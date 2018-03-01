package net.frozenchaos.catcoop.data;

import org.springframework.data.repository.CrudRepository;

public interface SettingRecordRepository extends CrudRepository<SettingRecord, Integer> {
    public SettingRecord findBySetting(String setting);
}
