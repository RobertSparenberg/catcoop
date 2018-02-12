package net.frozenchaos.catcoop.io;

import com.pi4j.io.gpio.*;

public class Lights extends Component {
    public void doSomething() {
        try {
            // get a handle to the GPIO controller
            final GpioController gpio = GpioFactory.getInstance();

            final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "PinLED", PinState.HIGH);
            final GpioPinPwmOutput pwm = gpio.provisionSoftPwmOutputPin(RaspiPin.GPIO_29, "servo");
            pwm.setPwm(50);

            // creating the pin with parameter PinState.HIGH
            // will instantly power up the pin
            System.out.println("light is: ON");

            // wait 2 seconds
            Thread.sleep(2000);

            // turn off GPIO 1
            pin.low();
            System.out.println("light is: OFF");

            // wait 1 second
            Thread.sleep(1000);

            // turn on GPIO 1 for 1 second and then off
            System.out.println("light is: ON for 1 second");
            pin.pulse(1000, true);

            Thread.sleep(1000);
            pwm.setPwm(10);
            Thread.sleep(2000);
            pwm.setPwm(120);
            Thread.sleep(2000);
            pwm.setPwm(50);

            // release the GPIO controller resources
            gpio.shutdown();


        } catch(Exception ignored) {
            //do nothing
        }
    }
}
