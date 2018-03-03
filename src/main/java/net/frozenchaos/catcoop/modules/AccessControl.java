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
import net.frozenchaos.catcoop.utils.ScheduledTask;
import net.frozenchaos.catcoop.utils.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccessControl implements IoComponentListener {
    private static final String UNKNOWN = "Unknown";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AccessRecordRepository accessRecordRepository;
    private final ImageRepository imageRepository;
    private final Timer timer;

    private MotionSensor motionSensor;
    private RfidReader rfidReader;
    private Door door;
    private Camera camera;
    private Light light;

    private List<Image> accessImages = new ArrayList<>();

    private ScheduledTask doorLockTask = null;
    private ScheduledTask accessRecordTask = null;
    private boolean snapshotsScheduled = false;

    @Autowired
    public AccessControl(IoManager ioManager, AccessRecordRepository accessRecordRepository, ImageRepository imageRepository, Timer timer) {
        logger.info("Initializing AccessControl module");
        this.motionSensor = new MotionSensor(ioManager, IoPin.MOTION_SENSOR);
        this.rfidReader = new RfidReader(ioManager);
        this.door = new Door(ioManager, IoPin.DOOR_SERVO);
        this.camera = new Camera(ioManager);
        this.light = new Light(ioManager, IoPin.LED1);

        this.accessRecordRepository = accessRecordRepository;
        this.imageRepository = imageRepository;
        this.timer = timer;
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
                scheduleSnapshots();
                scheduleSaveAccessRecord();
            } else {
                light.setLed(false);
            }
        }
    }

    @Override
    public void onIoComponentEvent(IoComponent component, String value) {
        logger.trace("Event on AccessControl module from component: "+component+", value: "+value);
        if(component == this.rfidReader) {
            if(value.equalsIgnoreCase("1234") || value.equalsIgnoreCase("2345")) {
                saveAccessRecord(value);
                door.unlock();
                scheduleLockingDoor();
            }
        }
    }

    private void scheduleSnapshots() {
        int numberOfSnapshots = Integer.valueOf(Setting.SNAPSHOTS_AMOUNT.getValue());
        int delayBetweenSnapshots = Integer.valueOf(Setting.SNAPSHOT_INTERVAL.getValue());
        logger.trace("Scheduling " + numberOfSnapshots + " snapshots " + delayBetweenSnapshots + "ms apart");
        if(!snapshotsScheduled) {
            for(int i = 0; i < numberOfSnapshots; i++) {
                scheduleSingleSnapshot(i*delayBetweenSnapshots);
            }
        }
        if(numberOfSnapshots > 0) {
            timer.addTask(new ScheduledTask((numberOfSnapshots+1)*delayBetweenSnapshots) {
                @Override
                public void doTask() {
                    snapshotsScheduled = false;
                }
            });
        }
    }

    private void scheduleSingleSnapshot(int millisecondsDelay) {
        if(millisecondsDelay == 0) {
            takeSnapshot();
        } else {
            timer.addTask(new ScheduledTask(millisecondsDelay) {
                @Override
                public void doTask() {
                    logger.trace("Taking snapshot");
                    takeSnapshot();
                }
            });
        }
    }

    private void scheduleSaveAccessRecord() {
        Integer motionSensorTimeout = Integer.valueOf(Setting.ACCESS_TIMEOUT.getValue());
        if(this.accessRecordTask == null) {
            logger.trace("Scheduling the saving of an access record in " + motionSensorTimeout + "ms");
            this.accessRecordTask = new ScheduledTask(motionSensorTimeout) {
                @Override
                public void doTask() {
                    logger.trace("Saving access record (due to timeout)");
                    saveAccessRecord(UNKNOWN);
                }
            };
            timer.addTask(this.accessRecordTask);
        } else {
            logger.trace("Resetting the saving of the access record to " + motionSensorTimeout + "ms");
            this.accessRecordTask.setDelay(motionSensorTimeout);
        }
    }

    private void scheduleLockingDoor() {
        int doorLockDelay = Integer.valueOf(Setting.DOOR_LOCK_DELAY.getValue());
        if(this.doorLockTask != null) {
            this.doorLockTask.setDelay(doorLockDelay);
        }
        this.doorLockTask = new ScheduledTask(doorLockDelay) {
            @Override
            public void doTask() {
                logger.trace("Locking door again");
                door.lock();
                doorLockTask = null;
            }
        };
        timer.addTask(this.doorLockTask);
    }

    private void saveAccessRecord(String cat) {
        AccessRecord accessRecord = new AccessRecord(cat, this.accessImages);
        this.accessImages.clear();
        this.accessRecordRepository.save(accessRecord);
        this.accessRecordTask.setNoLongerNeeded();
        this.accessRecordTask = null;
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
