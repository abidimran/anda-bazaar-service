package com.andabazaar.controller;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.entity.PriceHistory;
import com.andabazaar.service.PriceHistoryService;

import lombok.RequiredArgsConstructor;

@Tag(name = "Price History", description = "Historical price data")
@RestController
@RequestMapping("/api/price-history")
@RequiredArgsConstructor
public class PriceHistoryController {

    private final PriceHistoryService priceHistoryService;

    @Operation(summary = "Create")
    @PostMapping
    public ResponseEntity<PriceHistory>
    create(@RequestBody PriceHistory priceHistory) {

 return ResponseEntity.ok(priceHistoryService.createPriceHistory(priceHistory));
    }

    @Operation(summary = "Get By Id")
    @GetMapping("/{id}")
    public ResponseEntity<PriceHistory>
    getById(@PathVariable Long id) {

 return ResponseEntity.ok(priceHistoryService.getById(id));
    }

    @Operation(summary = "Get By Market")
    @GetMapping("/market/{marketId}")
    public ResponseEntity<List<PriceHistory>>
    getByMarket(@PathVariable Long marketId) {

 return ResponseEntity.ok(priceHistoryService.getByMarket(marketId));
    }

    @Operation(summary = "Get By Date Range")
    @GetMapping("/market/{marketId}/range")
    public ResponseEntity<List<PriceHistory>>
    getByDateRange(@PathVariable Long marketId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {

 return ResponseEntity.ok(priceHistoryService.getByMarketAndDateRange(marketId, startDate, endDate));
    }

    @Operation(summary = "Get By Date")
    @GetMapping("/date/{date}")
    public ResponseEntity<List<PriceHistory>>
    getByDate(@PathVariable LocalDate date) {

 return ResponseEntity.ok(priceHistoryService.getByDate(date));
    }

    @Operation(summary = "Delete")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    delete(@PathVariable Long id) {

        priceHistoryService.delete(id);

 return ResponseEntity.noContent().build();
    }
}