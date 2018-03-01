package net.frozenchaos.catcoop.configuration;

public enum Setting {
    SNAPSHOT_WIDTH("800"),
    SNAPSHOT_HEIGHT("600"),
    SNAPSHOTS_AMOUNT("3"),
    SNAPSHOT_INTERVAL("2"),
    ACCESS_TIMEOUT("15");

    private String value;

    private Setting(String defaultValue) {
        this.value = defaultValue;
    }

    public String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }
}
