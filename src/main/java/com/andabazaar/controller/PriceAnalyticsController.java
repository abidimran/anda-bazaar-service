package com.andabazaar.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.analytics.MarketStatisticsDto;
import com.andabazaar.dto.analytics.PriceAnalyticsResponseDto;
import com.andabazaar.dto.analytics.PriceTrendResponseDto;
import com.andabazaar.service.PriceAnalyticsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/price-analytics")
@RequiredArgsConstructor
public class PriceAnalyticsController {

    private final PriceAnalyticsService priceAnalyticsService;

    @GetMapping("/market/{marketId}")
    public ResponseEntity<PriceAnalyticsResponseDto>
    getMarketAnalytics(@PathVariable Long marketId, @RequestParam @DateTimeFormat( iso = DateTimeFormat.ISO.DATE )
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate endDate) {

 return ResponseEntity.ok(priceAnalyticsService.getMarketAnalytics(marketId, startDate, endDate));
    }

    @GetMapping("/market/{marketId}/statistics")
    public ResponseEntity<MarketStatisticsDto>
    getMarketStatistics(@PathVariable Long marketId) {

 return ResponseEntity.ok(priceAnalyticsService.getMarketStatistics(marketId));
    }

    @GetMapping("/market/{marketId}/trend")
    public ResponseEntity<List<PriceTrendResponseDto>>
    getPriceTrend(@PathVariable Long marketId, @RequestParam @DateTimeFormat( iso = DateTimeFormat.ISO.DATE )
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate endDate) {

 return ResponseEntity.ok(priceAnalyticsService.getPriceTrend(marketId, startDate, endDate));
    }

    @GetMapping("/markets")
    public ResponseEntity<List<MarketStatisticsDto>>
    getAllMarketStatistics() {

 return ResponseEntity.ok(priceAnalyticsService.getAllMarketStatistics());
    }
}