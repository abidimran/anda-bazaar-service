package com.andabazaar.service;

import com.andabazaar.dto.eggprice.EggPriceRequestDto;
import com.andabazaar.dto.eggprice.EggPriceResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface EggPriceService {
    EggPriceResponseDto createPrice(EggPriceRequestDto request);

    EggPriceResponseDto updatePrice(Long id, EggPriceRequestDto request);

    EggPriceResponseDto getPriceById(Long id);

    EggPriceResponseDto getMarketPrice(Long marketId, LocalDate date);

    List<EggPriceResponseDto> getTodayPrices();

    List<EggPriceResponseDto> getYesterdayPrices();

    List<EggPriceResponseDto> getPriceHistory(Long marketId, LocalDate startDate, LocalDate endDate);
    // USER - GET PRICES
    //
    // ACTIVE SUBSCRIPTION:
    // Today + Yesterday + Older
    //
    // NO/EXPIRED SUBSCRIPTION:
    List<EggPriceResponseDto> getUserPrices(Long userId);

    List<EggPriceResponseDto> getUserPriceHistory(Long userId, Long marketId, LocalDate startDate, LocalDate endDate);

    void deletePrice(Long id);
}
