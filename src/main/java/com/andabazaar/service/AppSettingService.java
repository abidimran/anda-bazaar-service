package com.andabazaar.service;

import java.util.List;

import com.andabazaar.dto.appsetting.AppSettingRequestDto;
import com.andabazaar.dto.appsetting.AppSettingResponseDto;

public interface AppSettingService {

    AppSettingResponseDto createSetting(
            AppSettingRequestDto request);

    AppSettingResponseDto updateSetting(
            Long id,
            AppSettingRequestDto request);

    AppSettingResponseDto getSettingById(
            Long id);

    AppSettingResponseDto getSettingByKey(
            String key);

    List<AppSettingResponseDto> getAllSettings();

    List<AppSettingResponseDto> getActiveSettings();

    void deactivateSetting(Long id);

    void deleteSetting(Long id);
}