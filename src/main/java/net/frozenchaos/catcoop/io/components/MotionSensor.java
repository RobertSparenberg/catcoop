package net.frozenchaos.catcoop.io.components;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import net.frozenchaos.catcoop.io.IoComponentListener;
import net.frozenchaos.catcoop.io.IoManager;
import net.frozenchaos.catcoop.io.Sensor;

public class MotionSensor extends Sensor {
    private final IoPin pin;
    private GpioPinDigitalInput input;

    public MotionSensor(IoManager ioManager, IoPin pin) {
        super(ioManager);
        this.pin = pin;
    }

    @Override
    public void init() {
        System.out.println("Motion sensor initialized");
        input = this.getIoManager().getGpioController().provisionDigitalInputPin(this.pin.getPin());
        input.addListener(new MotionSensorInputPinListener());
    }

    @Override
    public void destroy() {
        System.out.println("Destroying motion sensor");
        this.getIoManager().getGpioController().unprovisionPin(input);
    }

    private class MotionSensorInputPinListener implements GpioPinListenerDigital {
        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
            System.out.println("Motion sensor: " + event.getState());
            for(IoComponentListener listener : MotionSensor.this.getListeners()) {
                listener.onIoComponentEvent(MotionSensor.this, event.getState().isHigh() ? 1 : 0);
            }
        }
    }
}
