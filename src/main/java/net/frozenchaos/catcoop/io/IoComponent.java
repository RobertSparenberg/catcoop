package net.frozenchaos.catcoop.io;

/**
 * A component used to communicate with io pins.
 * It registers itself with the {@link IoManager} so that the init method is called the moment the io pins are initialized.
 * The destroy method will be called when the IoManager is being destroyed, so the pins can be reset to a default on
 * shutdown of the application.
 */
public abstract class IoComponent {
    private final IoManager ioManager;

    /**
     * Constructor that registers this component with the {@link IoManager}.
     * @param ioManager The {@link IoManager} to register this component with.
     */
    public IoComponent(IoManager ioManager) {
        ioManager.registerIoComponent(this);
        this.ioManager = ioManager;
    }

    /**
     * Get the {@link IoManager} to register individual io pins for use by this component.
     * @return The {@link IoManager} that this component is registered with.
     */
    public IoManager getIoManager() {
        return ioManager;
    }

    /**
     * Called after the {@link IoManager} has initialized the io pins, so that it is ready to have pins registered.
     * Pins should be initialized for this object inside of this method.
     */
    public abstract void init();

    /**
     * Clean up the pin usage of this component in this method.
     * This method is called right before the {@link IoManager} releases the io pins.
     */
    public abstract void destroy();
}
