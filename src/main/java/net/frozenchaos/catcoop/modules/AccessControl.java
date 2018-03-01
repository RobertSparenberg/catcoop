package net.frozenchaos.catcoop.modules;

import net.frozenchaos.catcoop.configuration.Setting;
import net.frozenchaos.catcoop.data.AccessRecord;
import net.frozenchaos.catcoop.data.AccessRecordRepository;
import net.frozenchaos.catcoop.data.Image;
import net.frozenchaos.catcoop.data.ImageRepository;
import net.frozenchaos.catcoop.io.IoComponent;
import net.frozenchaos.catcoop.io.IoComponentListener;
import net.frozenchaos.catcoop.io.IoManager;
import net.frozenchaos.catcoop.io.components.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccessControl implements IoComponentListener {
    private static final String UNKNOWN = "Unknown";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AccessRecordRepository accessRecordRepository;
    private final ImageRepository imageRepository;

    private MotionSensor motionSensor;
    private RfidReader rfidReader;
    private Door door;
    private Camera camera;
    private Light light;

    private List<Image> accessImages = new ArrayList<>();

    private int accessTimeout = 0;
    private int doorLockTimeout = 0;

    @Autowired
    public AccessControl(IoManager ioManager, AccessRecordRepository accessRecordRepository, ImageRepository imageRepository) {
        logger.info("Initializing AccessControl module");
        this.motionSensor = new MotionSensor(ioManager, IoPin.MOTION_SENSOR);
        this.rfidReader = new RfidReader(ioManager);
        this.door = new Door(ioManager, IoPin.DOOR_SERVO);
        this.camera = new Camera(ioManager);
        this.light = new Light(ioManager, IoPin.LED1);

        this.accessRecordRepository = accessRecordRepository;
        this.imageRepository = imageRepository;
        logger.info("Done initializing AccessControl module");

        this.motionSensor.addListener(this);
        this.rfidReader.addListener(this);
    }

    @Override
    public void onIoComponentEvent(IoComponent component, int value) {
        logger.trace("Event on AccessControl module from component: " + component + ", value: " + value);
        if(component == this.motionSensor) {
            if(value == 1) {
                this.light.setLed(true);
                takeSnapshot();
                this.accessTimeout = Integer.valueOf(Setting.ACCESS_TIMEOUT.getValue());
            } else {
                light.setLed(false);
            }
        }
    }

    @Override
    public void onIoComponentEvent(IoComponent component, String value) {
        logger.trace("Event on AccessControl module from component: " + component + ", value: " + value);
        if(component == this.rfidReader) {
            if(value.equalsIgnoreCase("1234") || value.equalsIgnoreCase("2345")) {
                saveAccessRecord(value);
                door.unlock();
                this.doorLockTimeout = 5;
            }
        }
    }

    //todo: remove the @Scheduled and replace with something more fidelity and control (separate Thread?)
    @Scheduled(fixedRate = 1000)
    public void timer() {
        if(this.doorLockTimeout > 0) {
            this.doorLockTimeout -= 1;
            if(this.doorLockTimeout == 0) {
                door.lock();
            }
        }
        if(this.accessTimeout > 0) {
            this.accessTimeout -= 1;
            if(this.accessTimeout == 0) {
                saveAccessRecord(UNKNOWN);
            }
        }
    }

    private void saveAccessRecord(String cat) {
        AccessRecord accessRecord = new AccessRecord(cat, this.accessImages);
        this.accessImages.clear();
        this.accessRecordRepository.save(accessRecord);
    }

    private void takeSnapshot() {
        int width = Integer.valueOf(Setting.SNAPSHOT_WIDTH.getValue());
        int height = Integer.valueOf(Setting.SNAPSHOT_HEIGHT.getValue());
        byte[] data = this.camera.takeSnapshot(width, height);
        Image image = new Image(data);
        this.imageRepository.save(image);
        this.accessImages.add(image);
    }
}
