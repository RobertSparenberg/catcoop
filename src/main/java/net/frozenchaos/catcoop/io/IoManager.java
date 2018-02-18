package net.frozenchaos.catcoop.io;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IoManager {
    private final GpioController gpioController;
    private List<IoComponent> ioComponents = new ArrayList<>();

    public IoManager() {
        this.gpioController = GpioFactory.getInstance();
    }

    public GpioController getGpioController() {
        return gpioController;
    }

    public void registerIoComponent(IoComponent ioComponent) {
        this.ioComponents.add(ioComponent);
    }

    @PostConstruct
    public void init() {
        for(IoComponent ioComponent : ioComponents) {
            ioComponent.init();
        }
    }

    @PreDestroy
    public void destroy() {
        for(IoComponent ioComponent : ioComponents) {
            ioComponent.destroy();
        }
        this.gpioController.shutdown();
    }
}
