/**
 * Web Controller for configuring the CatCoop.
 * @author imahilus
 */
package net.frozenchaos.catcoop.web;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import net.frozenchaos.catcoop.io.IoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/pintester")
public class PinTesterController {
    private static final String PIN_TESTER_TEMPLATE = "pinTester";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private IoManager ioManager;
    private Map<Integer, GpioPinDigital> pins = new HashMap<>();

    @Autowired
    public PinTesterController(IoManager ioManager) {
        logger.info("Initializing PinTesterController");
        this.ioManager = ioManager;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String mainPage() {
        return PIN_TESTER_TEMPLATE;
    }

    @RequestMapping(value = "/setpin", method = RequestMethod.GET)
    public String setPin(@RequestParam(value="pin") int pin,
                       @RequestParam(value="output", required = false, defaultValue = "true") boolean isOutput,
                       @RequestParam(value="value", required = false, defaultValue = "1") int value) {
        logger.trace("ControlPanelController.setPin pin: " + pin + ", isOutput: " + isOutput + ", value: " + value);
        GpioController gpioController = ioManager.getGpioController();
        releasePin(gpioController, pin);

        Pin gpioPinNumber = RaspiPin.getPinByAddress(pin);
        if(isOutput) {
            GpioPinDigitalOutput gpioPin = gpioController.provisionDigitalOutputPin(gpioPinNumber);
            gpioPin.setState(value == 1);
            pins.put(pin, gpioPin);
        } else {
            GpioPinDigitalInput gpioPin = gpioController.provisionDigitalInputPin(gpioPinNumber);
            gpioPin.addListener(new InputPinListener(pin));
            pins.put(pin, gpioPin);
        }
        return PIN_TESTER_TEMPLATE;
    }

    private void releasePin(GpioController gpioController, int pin) {
        GpioPinDigital gpioPin = pins.get(pin);
        if(gpioPin != null) {
            gpioController.unprovisionPin(gpioPin);
        }
    }

    private static class InputPinListener implements GpioPinListenerDigital {
        private int pinNumber;

        private InputPinListener(int pinNumber) {
            this.pinNumber = pinNumber;
        }

        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
            System.out.println("Pin " + this.pinNumber + ": " + event.getState());
        }
    }
}
