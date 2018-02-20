package net.frozenchaos.catcoop.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class Sensor extends IoComponent {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private List<IoComponentListener> listeners = new ArrayList<>();

    public Sensor(IoManager ioManager) {
        super(ioManager);
    }

    public void addListener(IoComponentListener listener) {
        logger.trace(this.getClass().getSimpleName() + " has registered " + listener.getClass().getSimpleName() + " as a listener");
        this.listeners.add(listener);
    }

    protected List<IoComponentListener> getListeners() {
        return listeners;
    }
}
