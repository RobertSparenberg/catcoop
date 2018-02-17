package net.frozenchaos.catcoop.io;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;

@Repository
public class IoManager {
    private final GpioController gpioController;

    public IoManager() {
        this.gpioController = GpioFactory.getInstance();
    }

    public GpioController getGpioController() {
        return gpioController;
    }

    @PreDestroy
    public void teardown() {
        this.gpioController.shutdown();
    }
}
