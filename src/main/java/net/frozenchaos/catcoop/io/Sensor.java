package net.frozenchaos.catcoop.io;

import java.util.ArrayList;
import java.util.List;

public abstract class Sensor extends IoComponent {
    private List<IoComponentListener> listeners = new ArrayList<>();

    public Sensor(IoManager ioManager) {
        super(ioManager);
    }

    public void addListener(IoComponentListener listener) {
        this.listeners.add(listener);
    }

    protected List<IoComponentListener> getListeners() {
        return listeners;
    }
}
