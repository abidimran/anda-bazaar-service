package com.andabazaar.service;

import java.time.LocalDate;
import java.util.List;

import com.andabazaar.dto.expectedprice.ExpectedPriceRequestDto;
import com.andabazaar.dto.expectedprice.ExpectedPriceResponseDto;

public interface ExpectedPriceService {

    // =========================
    // CREATE
    // =========================

    ExpectedPriceResponseDto createExpectedPrice(
            ExpectedPriceRequestDto request
    );

    // =========================
    // UPDATE
    // =========================

    ExpectedPriceResponseDto updateExpectedPrice(
            Long id,
            ExpectedPriceRequestDto request
    );

    // =========================
    // GET BY ID
    // =========================

    ExpectedPriceResponseDto getExpectedPriceById(
            Long id
    );

    // =========================
    // GET BY MARKET
    // =========================

    List<ExpectedPriceResponseDto> getByMarket(
            Long marketId
    );

    // =========================
    // GET BY MARKET + DATE
    // =========================

    ExpectedPriceResponseDto getByMarketAndDate(
            Long marketId,
            LocalDate expectedDate
    );

    // =========================
    // GET ACTIVE
    // =========================

    List<ExpectedPriceResponseDto> getActiveExpectedPrices();

    // =========================
    // DATE RANGE
    // =========================

    List<ExpectedPriceResponseDto> getByDateRange(
            LocalDate startDate,
            LocalDate endDate
    );

    // =========================
    // MARKET + DATE RANGE
    // =========================

    List<ExpectedPriceResponseDto> getMarketDateRange(
            Long marketId,
            LocalDate startDate,
            LocalDate endDate
    );

    // =========================
    // DELETE / DEACTIVATE
    // =========================

    void deleteExpectedPrice(
            Long id
    );

    // =========================
    // COUNT
    // =========================

    long countActiveExpectedPrices();
}