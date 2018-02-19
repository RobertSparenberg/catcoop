package net.frozenchaos.catcoop.io.components;

import com.pi4j.io.gpio.GpioPinPwmOutput;
import net.frozenchaos.catcoop.io.IoComponent;
import net.frozenchaos.catcoop.io.IoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class Door extends IoComponent {
    private static final int UNLOCKED_PWM_VALUE = 20;
    private static final int LOCKED_PWM_VALUE = 1000;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final IoPin pin;
    private GpioPinPwmOutput servoPin;
    private int unlocked = 0;

    public Door(IoManager ioManager, IoPin pin) {
        super(ioManager);
        this.pin = pin;
    }

    @Override
    public void init() {
        logger.debug("Initializing Door on pin: " + this.pin.getPin().getAddress());
        servoPin = this.getIoManager().getGpioController().provisionPwmOutputPin(pin.getPin());
    }

    @Override
    public void destroy() {
        logger.debug("Destroying Door on pin: " + this.pin.getPin().getAddress());
        this.getIoManager().getGpioController().unprovisionPin(servoPin);
    }

    public void unlock() {
        logger.trace("Unlocking Door on pin: " + this.pin.getPin().getAddress());
        servoPin.setPwm(UNLOCKED_PWM_VALUE);
        this.unlocked = 5;
    }

    @Scheduled(fixedRate = 1000)
    public void lock() {
        if(this.unlocked > 0) {
            this.unlocked -= 1;
            if(this.unlocked == 0) {
                logger.trace("Locking Door on pin: " + this.pin.getPin().getAddress());
                servoPin.setPwm(LOCKED_PWM_VALUE);
            }
        }
    }
}
