package net.frozenchaos.catcoop.catflap;

import net.frozenchaos.catcoop.io.IoComponent;
import net.frozenchaos.catcoop.io.IoComponentListener;
import net.frozenchaos.catcoop.io.IoManager;
import net.frozenchaos.catcoop.io.components.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccessControl implements IoComponentListener {
    private MotionSensor motionSensor;
    private RfidReader rfidReader;
    private Door door;
    private Camera camera;

    @Autowired
    public AccessControl(IoManager ioManager) {
        this.motionSensor = new MotionSensor(ioManager, IoPin.MOTION_SENSOR);
        this.rfidReader = new RfidReader(ioManager);
        this.door = new Door(ioManager, IoPin.DOOR_SERVO);
        this.camera = new Camera();

        this.motionSensor.addListener(this);
    }

    @Override
    public void onIoComponentEvent(IoComponent component, int value) {
        if(component == this.motionSensor && value == 1) {
            camera.takeSnapshot();
        }
    }

    @Override
    public void onIoComponentEvent(IoComponent component, String value) {
        if(component == this.rfidReader) {
            if(value.equalsIgnoreCase("1234") || value.equalsIgnoreCase("2345")) {
                door.unlock();
            }
        }
    }
}
