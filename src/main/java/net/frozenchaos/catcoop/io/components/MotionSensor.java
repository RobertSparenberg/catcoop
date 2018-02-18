package net.frozenchaos.catcoop.io.components;

import com.pi4j.io.gpio.GpioPinDigitalInput;
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
        input = this.getIoManager().getGpioController().provisionDigitalInputPin(pin.getPin());
        input.addListener((GpioPinListenerDigital) event -> {
            for(IoComponentListener listener : this.getListeners()) {
                listener.onIoComponentEvent(this, event.getState().isHigh() ? 1 : 0);
            }
        });
    }

    @Override
    public void destroy() {
        if(input != null) {
            this.getIoManager().getGpioController().unprovisionPin(input);
        }
    }
}
