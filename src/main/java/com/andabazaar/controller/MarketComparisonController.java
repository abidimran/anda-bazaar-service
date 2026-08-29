package com.andabazaar.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.comparison.MarketComparisonResponseDto;
import com.andabazaar.service.MarketComparisonService;

import lombok.RequiredArgsConstructor;

@Tag(name = "Market Comparison", description = "Compare egg prices across markets")
@RestController
@RequestMapping("/api/market-comparison")
@RequiredArgsConstructor
public class MarketComparisonController {

    private final MarketComparisonService marketComparisonService;

    @Operation(summary = "Compare Markets")
    @GetMapping
    public ResponseEntity<List<MarketComparisonResponseDto>>
            compareMarkets() {

 return ResponseEntity.ok(marketComparisonService.compareMarkets());
    }

    @Operation(summary = "Compare Market")
    @GetMapping("/{marketId}")
    public ResponseEntity<MarketComparisonResponseDto>
            compareMarket(@PathVariable Long marketId) {

 return ResponseEntity.ok(marketComparisonService.compareMarket(marketId));
    }
}