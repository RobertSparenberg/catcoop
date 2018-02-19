package net.frozenchaos.catcoop.io;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IoManager implements ApplicationListener<ContextRefreshedEvent> {
    private final GpioController gpioController;
    private List<IoComponent> ioComponents = new ArrayList<>();
    private boolean started = false;

    public IoManager() {
        this.gpioController = GpioFactory.getInstance();
    }

    public GpioController getGpioController() {
        return gpioController;
    }

    public void registerIoComponent(IoComponent ioComponent) {
        System.out.println("registering component: " + ioComponent.getClass().getName());
        this.ioComponents.add(ioComponent);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(!this.started) {
            System.out.println("IoManager is going to initialize "+ioComponents.size()+" components");
            for(IoComponent ioComponent : ioComponents) {
                ioComponent.init();
            }
            started = true;
        }
    }

    @PreDestroy
    public void destroy() {
        System.out.println("IoManager is going to destroy components");
        for(IoComponent ioComponent : ioComponents) {
            ioComponent.destroy();
        }
        this.gpioController.shutdown();
    }
}
