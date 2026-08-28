package com.andabazaar.service;

import java.util.List;

import com.andabazaar.dto.market.MarketRequestDto;
import com.andabazaar.dto.market.MarketResponseDto;

public interface MarketService {

    MarketResponseDto createMarket(
            MarketRequestDto request);

    MarketResponseDto getMarketById(Long id);

    List<MarketResponseDto> getAllMarkets();

    List<MarketResponseDto> getMarketsByCity(
            Long cityId);

    MarketResponseDto updateMarket(
            Long id,
            MarketRequestDto request);

    void deleteMarket(Long id);
}