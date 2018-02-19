package net.frozenchaos.catcoop.io.components;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import net.frozenchaos.catcoop.io.IoComponentListener;
import net.frozenchaos.catcoop.io.IoManager;
import net.frozenchaos.catcoop.io.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MotionSensor extends Sensor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final IoPin pin;
    private GpioPinDigitalInput input;

    public MotionSensor(IoManager ioManager, IoPin pin) {
        super(ioManager);
        this.pin = pin;
    }

    @Override
    public void init() {
        logger.debug("Initializing MotionSensor on pin: "+this.pin.getPin().getAddress());
        input = this.getIoManager().getGpioController().provisionDigitalInputPin(this.pin.getPin());
        input.addListener(new MotionSensorInputPinListener());
    }

    @Override
    public void destroy() {
        logger.debug("Destroying MotionSensor on pin: "+this.pin.getPin().getAddress());
        this.getIoManager().getGpioController().unprovisionPin(input);
    }

    private class MotionSensorInputPinListener implements GpioPinListenerDigital {
        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
            logger.trace("MotionSensor on pin: " + MotionSensor.this.pin.getPin().getAddress() + " changed to " + event.getState());
            for(IoComponentListener listener : MotionSensor.this.getListeners()) {
                listener.onIoComponentEvent(MotionSensor.this, event.getState().isHigh() ? 1 : 0);
            }
        }
    }

    @Override
    public String toString() {
        return "{\"ComponentClass\":\"" + this.getClass().getName() + "\",\"Pin\":" + this.pin.getPin().getAddress() + "\"}";
    }
}
