package net.frozenchaos.catcoop.configuration;

import net.frozenchaos.catcoop.data.SettingRecord;
import net.frozenchaos.catcoop.data.SettingRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class SettingsManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final SettingRecordRepository settingRecordRepository;

    @Autowired
    public SettingsManager(SettingRecordRepository settingRecordRepository) {
        this.settingRecordRepository = settingRecordRepository;
    }

    @PostConstruct
    public void init() {
        logger.info("Initializing SettingsManager");
//        if(this.settingRecordRepository.count() == 0) {
//            logger.info("Saving default settings to DB");
//            for(Setting setting : Setting.values()) {
//                setSettingValue(setting, setting.getValue());
//            }
//        } else {
//            logger.info("Retrieving default settings from DB");
//            for(SettingRecord setting : this.settingRecordRepository.findAll()) {
//                logger.trace("Setting '"+setting.getSetting()+"' set to '"+setting.getValue()+"'");
//                setting.getSetting().setValue(setting.getValue());
//            }
//        }
    }

    public void setSettingValue(Setting setting, String value) {
        setting.setValue(value);
        SettingRecord settingRecord = this.settingRecordRepository.findBySetting(setting.name());
        if(settingRecord == null) {
            logger.trace("Saving default value of '" + setting.getValue() + "' for Setting '" + setting.name() + "'");
            settingRecord = new SettingRecord(setting, value);
        }
        this.settingRecordRepository.save(settingRecord);
    }
}
