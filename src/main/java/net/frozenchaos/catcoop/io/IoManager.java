package net.frozenchaos.catcoop.io;

import com.pi4j.component.servo.ServoProvider;
import com.pi4j.component.servo.impl.RPIServoBlasterProvider;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IoManager implements ApplicationListener<ContextRefreshedEvent> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final GpioController gpioController;
    private final ServoProvider servoProvider;
    private List<IoComponent> ioComponents = new ArrayList<>();
    private boolean started = false;

    public IoManager() throws Exception {
        logger.debug("Initializing IoManager");
        this.gpioController = GpioFactory.getInstance();
        this.servoProvider = new RPIServoBlasterProvider();
    }

    public GpioController getGpioController() {
        return gpioController;
    }

    public ServoProvider getServoProvider() {
        return servoProvider;
    }

    public void registerIoComponent(IoComponent ioComponent) {
        logger.debug("IoManager is registering component: "+ioComponent.getClass().getName());
        this.ioComponents.add(ioComponent);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(!this.started) {
            logger.trace("IoManager is going to initialize "+ioComponents.size()+" components");
            for(IoComponent ioComponent : ioComponents) {
                ioComponent.init();
            }
            started = true;
        }
    }

    @PreDestroy
    public void destroy() {
        logger.debug("Destroying IoManager");
        for(IoComponent ioComponent : ioComponents) {
            ioComponent.destroy();
        }
        this.gpioController.shutdown();
        logger.debug("Done destroying IoManager");
    }
}
