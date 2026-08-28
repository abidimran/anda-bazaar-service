package com.andabazaar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andabazaar.entity.AppSetting;

public interface AppSettingRepository
        extends JpaRepository<AppSetting, Long> {

    Optional<AppSetting> findBySettingKey(String settingKey);

    boolean existsBySettingKey(String settingKey);

    List<AppSetting> findByActiveTrueOrderBySettingKeyAsc();
}