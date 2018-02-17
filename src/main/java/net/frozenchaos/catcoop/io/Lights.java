package net.frozenchaos.catcoop.io;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class Lights {
    private IoManager ioManager;
    private GpioPinDigitalOutput led;

    @Autowired
    public Lights(IoManager ioManager) {
        this.ioManager = ioManager;
    }

    @PostConstruct
    public void init() {
        this.led = ioManager.getGpioController().provisionDigitalOutputPin(RaspiPin.GPIO_07, "LED", PinState.LOW);
    }

    @Scheduled(fixedRate = 1000)
    public void blink() {
        this.led.setState(!this.led.getState().isHigh());
    }
}
