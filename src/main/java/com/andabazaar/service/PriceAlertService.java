package com.andabazaar.service;

import java.util.List;

import com.andabazaar.dto.alert.PriceAlertRequestDto;
import com.andabazaar.dto.alert.PriceAlertResponseDto;

public interface PriceAlertService {

    PriceAlertResponseDto createAlert(
            PriceAlertRequestDto request
    );

    PriceAlertResponseDto getAlertById(
            Long id
    );

    List<PriceAlertResponseDto> getUserAlerts(
            Long userId
    );

    PriceAlertResponseDto updateAlert(
            Long id,
            PriceAlertRequestDto request
    );

    void deleteAlert(
            Long id
    );

    PriceAlertResponseDto toggleAlert(
            Long id
    );
}