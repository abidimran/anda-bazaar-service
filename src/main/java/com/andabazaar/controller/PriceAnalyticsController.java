package com.andabazaar.controller;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.analytics.MarketStatisticsDto;
import com.andabazaar.dto.analytics.PriceAnalyticsResponseDto;
import com.andabazaar.dto.analytics.PriceTrendResponseDto;
import com.andabazaar.dto.common.PagedResponse;
import com.andabazaar.service.PriceAnalyticsService;

import lombok.RequiredArgsConstructor;

@Tag(name = "Price Analytics", description = "Price trends and statistics")
@RestController
@RequestMapping("/api/price-analytics")
@RequiredArgsConstructor
public class PriceAnalyticsController {

    private final PriceAnalyticsService priceAnalyticsService;

    @Operation(summary = "Get Market Analytics")
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

    @Operation(summary = "Get Market Statistics")
    @GetMapping("/market/{marketId}/statistics")
    public ResponseEntity<MarketStatisticsDto>
    getMarketStatistics(@PathVariable Long marketId) {

 return ResponseEntity.ok(priceAnalyticsService.getMarketStatistics(marketId));
    }

    @Operation(summary = "Get Price Trend")
    @GetMapping("/market/{marketId}/trend")
    public ResponseEntity<PagedResponse<PriceTrendResponseDto>>
    getPriceTrend(@PathVariable Long marketId, @RequestParam @DateTimeFormat( iso = DateTimeFormat.ISO.DATE )
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate endDate,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(priceAnalyticsService.getPriceTrend(marketId, startDate, endDate), page, size));
    }

    @Operation(summary = "Get All Market Statistics")
    @GetMapping("/markets")
    public ResponseEntity<PagedResponse<MarketStatisticsDto>>
    getAllMarketStatistics(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(priceAnalyticsService.getAllMarketStatistics(), page, size));
    }
}
