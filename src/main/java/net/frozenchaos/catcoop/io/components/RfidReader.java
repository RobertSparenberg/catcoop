package net.frozenchaos.catcoop.io.components;

import net.frozenchaos.catcoop.io.IoManager;
import net.frozenchaos.catcoop.io.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RfidReader extends Sensor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public RfidReader(IoManager ioManager) {
        super(ioManager);
    }

    @Override
    public void init() {
        logger.debug("Initializing RfidReader");
    }

    @Override
    public void destroy() {
        logger.debug("Destroying RfidReader");
    }

    @Override
    public String toString() {
        return "{\"ComponentClass\":\"" + this.getClass().getName() + "\",\"Pin\":" + -1 + "\"}";
    }
}
