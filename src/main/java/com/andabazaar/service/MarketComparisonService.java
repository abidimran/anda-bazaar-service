package com.andabazaar.service;

import java.util.List;

import com.andabazaar.dto.comparison.MarketComparisonResponseDto;

public interface MarketComparisonService {

    List<MarketComparisonResponseDto> compareMarkets();

    MarketComparisonResponseDto compareMarket(Long marketId);
}