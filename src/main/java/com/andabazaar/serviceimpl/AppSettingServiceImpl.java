package com.andabazaar.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.dto.appsetting.AppSettingRequestDto;
import com.andabazaar.dto.appsetting.AppSettingResponseDto;
import com.andabazaar.entity.AppSetting;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.AppSettingRepository;
import com.andabazaar.service.AppSettingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AppSettingServiceImpl
        implements AppSettingService {

    private final AppSettingRepository appSettingRepository;

    @Override
    public AppSettingResponseDto createSetting( AppSettingRequestDto request) {

        String key = request.getSettingKey()
                .trim()
                .toUpperCase();

        if (appSettingRepository
                .existsBySettingKey(key)) {

            throw new BadRequestException(
                    "Setting key already exists");
        }

        AppSetting setting = AppSetting.builder()
                .settingKey(key)
                .settingValue(request.getSettingValue())
                .description(request.getDescription())
                .active( request.getActive() == null ? true : request.getActive()
                )
                .build();

        return mapToResponse(
                appSettingRepository.save(setting));
    }

    @Override
    public AppSettingResponseDto updateSetting( Long id, AppSettingRequestDto request) {

        AppSetting setting = findSetting(id);

        String key = request.getSettingKey()
                .trim()
                .toUpperCase();

        if (!setting.getSettingKey()
                .equalsIgnoreCase(key)
                && appSettingRepository
                        .existsBySettingKey(key)) {

            throw new BadRequestException(
                    "Setting key already exists");
        }

        setting.setSettingKey(key);
        setting.setSettingValue( request.getSettingValue());
        setting.setDescription( request.getDescription());

        if (request.getActive() != null) {
            setting.setActive( request.getActive());
        }

        return mapToResponse(
                appSettingRepository.save(setting));
    }

    @Override
    @Transactional(readOnly = true)
    public AppSettingResponseDto getSettingById( Long id) {

        return mapToResponse(
                findSetting(id));
    }

    @Override
    @Transactional(readOnly = true)
    public AppSettingResponseDto getSettingByKey( String key) {

        AppSetting setting =
                appSettingRepository
                        .findBySettingKey( key.trim().toUpperCase()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Setting not found with key: "
                                                + key
                                ));

        return mapToResponse(setting);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppSettingResponseDto>
            getAllSettings() {

        return appSettingRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppSettingResponseDto>
            getActiveSettings() {

        return appSettingRepository
                .findByActiveTrueOrderBySettingKeyAsc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deactivateSetting(Long id) {

        AppSetting setting = findSetting(id);

        setting.setActive(false);

        appSettingRepository.save(setting);
    }

    @Override
    public void deleteSetting(Long id) {

        AppSetting setting = findSetting(id);

        appSettingRepository.delete(setting);
    }

    private AppSetting findSetting(Long id) {

        return appSettingRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Setting not found with id: "
                                        + id
                        ));
    }

    private AppSettingResponseDto mapToResponse( AppSetting setting) {

        return AppSettingResponseDto.builder()
                .id(setting.getId())
                .settingKey( setting.getSettingKey()
                )
                .settingValue( setting.getSettingValue()
                )
                .description( setting.getDescription()
                )
                .active( setting.getActive()
                )
                .createdAt( setting.getCreatedAt()
                )
                .updatedAt( setting.getUpdatedAt()
                )
                .build();
    }
}