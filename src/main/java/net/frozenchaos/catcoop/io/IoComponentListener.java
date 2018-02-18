package net.frozenchaos.catcoop.io;

public interface IoComponentListener {
    void onIoComponentEvent(IoComponent component, int value);
    void onIoComponentEvent(IoComponent component, String value);
}
