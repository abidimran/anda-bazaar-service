package com.andabazaar.service;

import java.time.LocalDate;
import java.util.List;

import com.andabazaar.dto.analytics.MarketStatisticsDto;
import com.andabazaar.dto.analytics.PriceAnalyticsResponseDto;
import com.andabazaar.dto.analytics.PriceTrendResponseDto;

public interface PriceAnalyticsService {

    PriceAnalyticsResponseDto getMarketAnalytics( Long marketId, LocalDate startDate, LocalDate endDate);

    MarketStatisticsDto getMarketStatistics( Long marketId);

    List<PriceTrendResponseDto> getPriceTrend( Long marketId, LocalDate startDate, LocalDate endDate);

    List<MarketStatisticsDto> getAllMarketStatistics();
}