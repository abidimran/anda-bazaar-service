package com.andabazaar.controller;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.common.PagedResponse;
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
    public ResponseEntity<PagedResponse<PriceHistory>>
    getByMarket(@PathVariable Long marketId,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(priceHistoryService.getByMarket(marketId), page, size));
    }

    @Operation(summary = "Get By Date Range")
    @GetMapping("/market/{marketId}/range")
    public ResponseEntity<PagedResponse<PriceHistory>>
    getByDateRange(@PathVariable Long marketId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(priceHistoryService.getByMarketAndDateRange(marketId, startDate, endDate), page, size));
    }

    @Operation(summary = "Get By Date")
    @GetMapping("/date/{date}")
    public ResponseEntity<PagedResponse<PriceHistory>>
    getByDate(@PathVariable LocalDate date,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(priceHistoryService.getByDate(date), page, size));
    }

    @Operation(summary = "Delete")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    delete(@PathVariable Long id) {

        priceHistoryService.delete(id);

 return ResponseEntity.noContent().build();
    }
}
