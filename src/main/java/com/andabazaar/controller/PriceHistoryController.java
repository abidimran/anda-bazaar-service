package com.andabazaar.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.entity.PriceHistory;
import com.andabazaar.service.PriceHistoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/price-history")
@RequiredArgsConstructor
public class PriceHistoryController {

    private final PriceHistoryService priceHistoryService;

    @PostMapping
    public ResponseEntity<PriceHistory>
    create(@RequestBody PriceHistory priceHistory) {

        return ResponseEntity.ok(
                priceHistoryService
                        .createPriceHistory(priceHistory)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PriceHistory>
    getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                priceHistoryService.getById(id)
        );
    }

    @GetMapping("/market/{marketId}")
    public ResponseEntity<List<PriceHistory>>
    getByMarket(
            @PathVariable Long marketId) {

        return ResponseEntity.ok(
                priceHistoryService
                        .getByMarket(marketId)
        );
    }

    @GetMapping("/market/{marketId}/range")
    public ResponseEntity<List<PriceHistory>>
    getByDateRange(
            @PathVariable Long marketId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return ResponseEntity.ok(
                priceHistoryService
                        .getByMarketAndDateRange(
                                marketId,
                                startDate,
                                endDate
                        )
        );
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<PriceHistory>>
    getByDate(
            @PathVariable LocalDate date) {

        return ResponseEntity.ok(
                priceHistoryService.getByDate(date)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    delete(@PathVariable Long id) {

        priceHistoryService.delete(id);

        return ResponseEntity.noContent().build();
    }
}