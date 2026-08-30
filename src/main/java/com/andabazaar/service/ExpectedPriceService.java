package com.andabazaar.service;

import com.andabazaar.dto.expectedprice.ExpectedPriceRequestDto;
import com.andabazaar.dto.expectedprice.ExpectedPriceResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface ExpectedPriceService {
    ExpectedPriceResponseDto createExpectedPrice(ExpectedPriceRequestDto request);

    ExpectedPriceResponseDto updateExpectedPrice(Long id, ExpectedPriceRequestDto request);

    ExpectedPriceResponseDto getExpectedPriceById(Long id);

    List<ExpectedPriceResponseDto> getByMarket(Long marketId);

    ExpectedPriceResponseDto getByMarketAndDate(Long marketId, LocalDate expectedDate);

    List<ExpectedPriceResponseDto> getActiveExpectedPrices();

    List<ExpectedPriceResponseDto> getByDateRange(LocalDate startDate, LocalDate endDate);

    List<ExpectedPriceResponseDto> getMarketDateRange(Long marketId, LocalDate startDate, LocalDate endDate);

    void deleteExpectedPrice(Long id);

    long countActiveExpectedPrices();
}
