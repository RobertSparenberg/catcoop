package net.frozenchaos.catcoop.data;

import net.frozenchaos.catcoop.configuration.Setting;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "settings")
public class SettingRecord implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, updatable = false)
    @Enumerated(value = EnumType.STRING)
    private Setting setting;

    @Column(nullable = false)
    private String value;

    public SettingRecord(Setting setting, String value) {
        this.setting = setting;
        this.value = value;
    }

    public Setting getSetting() {
        return setting;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
