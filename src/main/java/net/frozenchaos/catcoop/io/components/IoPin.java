package net.frozenchaos.catcoop.io.components;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public enum IoPin {
    MOTION_SENSOR(RaspiPin.GPIO_04),
    DOOR_SERVO(RaspiPin.GPIO_26);

    private final Pin componentPin;

    IoPin(Pin componentPin) {
        this.componentPin = componentPin;
    }

    public Pin getPin() {
        return componentPin;
    }
}
