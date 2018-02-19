package net.frozenchaos.catcoop.io.components;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import net.frozenchaos.catcoop.io.IoComponent;
import net.frozenchaos.catcoop.io.IoManager;
import org.springframework.scheduling.annotation.Scheduled;

public class Lights extends IoComponent {
    private final IoPin pin;
    private GpioPinDigitalOutput led;

    public Lights(IoManager ioManager, IoPin pin) {
        super(ioManager);
        this.pin = pin;
    }

    @Override
    public void init() {
        System.out.println("Lights initialized");
        this.led = this.getIoManager().getGpioController().provisionDigitalOutputPin(this.pin.getPin());
        this.led.setState(false);
    }

    @Override
    public void destroy() {
        System.out.println("Destroying lights");
        this.led.setState(false);
        this.getIoManager().getGpioController().unprovisionPin(led);

    }

    public void setLed(boolean on) {
        this.led.setState(on);
    }

    @Scheduled(fixedRate = 1000)
    public void blink() {
        this.led.setState(this.led.getState().isLow());
    }
}
