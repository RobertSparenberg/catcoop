package net.frozenchaos.catcoop.configuration;

public enum Setting {
    PIN_NUMBER,
    PIN_VALUE,
    IS_OUTPUT;

    private int value = 0;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
