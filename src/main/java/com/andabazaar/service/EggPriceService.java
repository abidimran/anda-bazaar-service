package com.andabazaar.service;

import java.time.LocalDate;
import java.util.List;

import com.andabazaar.dto.eggprice.EggPriceRequestDto;
import com.andabazaar.dto.eggprice.EggPriceResponseDto;

public interface EggPriceService {

    // =====================================================
    // ADMIN - CREATE PRICE
    // =====================================================

    EggPriceResponseDto createPrice( EggPriceRequestDto request);

    // =====================================================
    // ADMIN - UPDATE PRICE
    // =====================================================

    EggPriceResponseDto updatePrice( Long id, EggPriceRequestDto request);

    // =====================================================
    // GET PRICE BY ID
    // =====================================================

    EggPriceResponseDto getPriceById( Long id);

    // =====================================================
    // GET MARKET PRICE BY DATE
    // =====================================================

    EggPriceResponseDto getMarketPrice( Long marketId, LocalDate date);

    // =====================================================
    // GET TODAY PRICES
    // =====================================================

    List<EggPriceResponseDto> getTodayPrices();

    // =====================================================
    // GET YESTERDAY PRICES
    // =====================================================

    List<EggPriceResponseDto> getYesterdayPrices();

    // =====================================================
    // GET PUBLIC/ADMIN PRICE HISTORY
    // =====================================================

    List<EggPriceResponseDto> getPriceHistory( Long marketId, LocalDate startDate, LocalDate endDate);

    // =====================================================
    // USER - GET PRICES
    //
    // ACTIVE SUBSCRIPTION:
    // Today + Yesterday + Older
    //
    // NO/EXPIRED SUBSCRIPTION:
    // 2 DAYS OLD + OLDER
    // =====================================================

    List<EggPriceResponseDto> getUserPrices( Long userId);

    // =====================================================
    // USER - GET PRICE HISTORY
    // =====================================================

    List<EggPriceResponseDto> getUserPriceHistory( Long userId, Long marketId, LocalDate startDate, LocalDate endDate);

    // =====================================================
    // ADMIN - DELETE PRICE
    // =====================================================

    void deletePrice( Long id);
}