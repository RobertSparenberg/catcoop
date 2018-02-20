package net.frozenchaos.catcoop.modules;

import net.frozenchaos.catcoop.io.IoComponent;
import net.frozenchaos.catcoop.io.IoComponentListener;
import net.frozenchaos.catcoop.io.IoManager;
import net.frozenchaos.catcoop.io.components.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AccessControl implements IoComponentListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private MotionSensor motionSensor;
    private RfidReader rfidReader;
    private Door door;
    private Camera camera;
    private Light light;

    private int doorUnlocked = 0;

    @Autowired
    public AccessControl(IoManager ioManager) {
        logger.info("Initializing AccessControl module");
        this.motionSensor = new MotionSensor(ioManager, IoPin.MOTION_SENSOR);
        this.rfidReader = new RfidReader(ioManager);
        this.door = new Door(ioManager, IoPin.DOOR_SERVO);
        this.camera = new Camera(ioManager);
        this.light = new Light(ioManager, IoPin.LED1);
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
                this.camera.takeSnapshot();
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
                door.unlock();
                this.doorUnlocked = 5;
            }
        }
    }

    @Scheduled(fixedRate = 1000)
    public void lockDoor() {
        if(this.doorUnlocked > 0) {
            this.doorUnlocked -= 1;
            if(this.doorUnlocked == 0) {
                door.lock();
            }
        }
    }
}
