package net.frozenchaos.catcoop.io.components;

import net.frozenchaos.catcoop.io.ComponentInitializationException;
import net.frozenchaos.catcoop.io.IoComponent;
import net.frozenchaos.catcoop.io.IoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Door extends IoComponent {
    private static final float UNLOCKED_VALUE = 90;
    private static final float LOCKED_VALUE = -90;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final IoPin pin;

    public Door(IoManager ioManager, IoPin pin) {
        super(ioManager);
        this.pin = pin;
    }

    @Override
    public void init() throws ComponentInitializationException {
        logger.debug("Initializing Door on pin: " + this.pin.getPin().getAddress());
        try {
            //todo: add servo control
        } catch(Exception e) {
            throw new ComponentInitializationException(e);
        }
    }

    @Override
    public void destroy() {
        logger.debug("Destroying Door on pin: " + this.pin.getPin().getAddress());
    }

    public void unlock() {
        logger.trace("Unlocking Door on pin: "+this.pin.getPin().getAddress());
        //todo: add servo control
    }

    public void lock() {
        logger.trace("Locking Door on pin: " + this.pin.getPin().getAddress());
        //todo: add servo control
    }
}
