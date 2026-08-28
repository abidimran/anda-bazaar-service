package com.andabazaar.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.comparison.MarketComparisonResponseDto;
import com.andabazaar.service.MarketComparisonService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/market-comparison")
@RequiredArgsConstructor
public class MarketComparisonController {

    private final MarketComparisonService marketComparisonService;

    @GetMapping
    public ResponseEntity<List<MarketComparisonResponseDto>>
            compareMarkets() {

 return ResponseEntity.ok(marketComparisonService.compareMarkets());
    }

    @GetMapping("/{marketId}")
    public ResponseEntity<MarketComparisonResponseDto>
            compareMarket(@PathVariable Long marketId) {

 return ResponseEntity.ok(marketComparisonService.compareMarket(marketId));
    }
}