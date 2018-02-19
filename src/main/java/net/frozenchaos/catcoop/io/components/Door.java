package net.frozenchaos.catcoop.io.components;

import com.pi4j.io.gpio.GpioPinPwmOutput;
import net.frozenchaos.catcoop.io.IoComponent;
import net.frozenchaos.catcoop.io.IoManager;
import org.springframework.scheduling.annotation.Scheduled;

public class Door extends IoComponent {
    private static final int UNLOCKED_PWM_VALUE = 20;
    private static final int LOCKED_PWM_VALUE = 1000;

    private final IoPin pin;
    private GpioPinPwmOutput servoPin;
    private int unlocked = 0;

    public Door(IoManager ioManager, IoPin pin) {
        super(ioManager);
        this.pin = pin;
    }

    @Override
    public void init() {
        System.out.println("Initializing door");
        servoPin = this.getIoManager().getGpioController().provisionPwmOutputPin(pin.getPin());
    }

    @Override
    public void destroy() {
        System.out.println("Destroying door");
        this.getIoManager().getGpioController().unprovisionPin(servoPin);
    }

    public void unlock() {
        servoPin.setPwm(UNLOCKED_PWM_VALUE);
        this.unlocked = 5;
    }

    @Scheduled(fixedRate = 1000)
    public void lock() {
        if(this.unlocked > 0) {
            this.unlocked -= 1;
            if(this.unlocked == 0) {
                servoPin.setPwm(LOCKED_PWM_VALUE);
            }
        }
    }
}
