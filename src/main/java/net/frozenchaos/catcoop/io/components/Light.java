package net.frozenchaos.catcoop.io.components;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import net.frozenchaos.catcoop.io.IoComponent;
import net.frozenchaos.catcoop.io.IoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Light extends IoComponent {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final IoPin pin;
    private GpioPinDigitalOutput led;

    public Light(IoManager ioManager, IoPin pin) {
        super(ioManager);
        this.pin = pin;
    }

    @Override
    public void init() {
        logger.debug("Initializing Light on pin: " + this.pin.getPin().getAddress());
        this.led = this.getIoManager().getGpioController().provisionDigitalOutputPin(this.pin.getPin());
        this.led.setState(false);
    }

    @Override
    public void destroy() {
        logger.debug("Destroying Light on pin: " + this.pin.getPin().getAddress());
        this.led.setState(false);
        this.getIoManager().getGpioController().unprovisionPin(led);

    }

    public void setLed(boolean on) {
        logger.trace("Setting Light on pin: " + this.pin.getPin().getAddress() + " to " + on);
        this.led.setState(on);
    }
}
