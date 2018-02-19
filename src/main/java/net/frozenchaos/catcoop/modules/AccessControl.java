package net.frozenchaos.catcoop.modules;

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
    private Lights lights;

    @Autowired
    public AccessControl(IoManager ioManager) {
        System.out.println("Starting access control");

        this.motionSensor = new MotionSensor(ioManager, IoPin.MOTION_SENSOR);
        this.rfidReader = new RfidReader(ioManager);
        this.door = new Door(ioManager, IoPin.DOOR_SERVO);
        this.camera = new Camera(ioManager);
        this.lights = new Lights(ioManager, IoPin.LED1);

        this.motionSensor.addListener(this);
        this.rfidReader.addListener(this);
    }

    @Override
    public void onIoComponentEvent(IoComponent component, int value) {
        if(component == this.motionSensor) {
            if(value == 1) {
                lights.setLed(true);
                camera.takeSnapshot();
            } else {
                lights.setLed(false);
            }
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
