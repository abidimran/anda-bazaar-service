package com.andabazaar.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.expectedprice.ExpectedPriceRequestDto;
import com.andabazaar.dto.expectedprice.ExpectedPriceResponseDto;
import com.andabazaar.service.ExpectedPriceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/expected-prices")
@RequiredArgsConstructor
public class ExpectedPriceController {

    private final ExpectedPriceService expectedPriceService;

    // =========================================================
    // CREATE
    // =========================================================

    @PostMapping
    public ResponseEntity<ExpectedPriceResponseDto>
            createExpectedPrice(@RequestBody ExpectedPriceRequestDto request) {

 return ResponseEntity.ok(expectedPriceService.createExpectedPrice(request));
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<ExpectedPriceResponseDto>
            updateExpectedPrice(@PathVariable Long id, @RequestBody ExpectedPriceRequestDto request) {

 return ResponseEntity.ok(expectedPriceService.updateExpectedPrice(id, request));
    }

    // =========================================================
    // GET BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<ExpectedPriceResponseDto>
            getExpectedPriceById(@PathVariable Long id) {

 return ResponseEntity.ok(expectedPriceService.getExpectedPriceById(id));
    }

    // =========================================================
    // GET BY MARKET
    // =========================================================

    @GetMapping("/market/{marketId}")
    public ResponseEntity<List<ExpectedPriceResponseDto>>
            getByMarket(@PathVariable Long marketId) {

 return ResponseEntity.ok(expectedPriceService.getByMarket(marketId));
    }

    // =========================================================
    // GET BY MARKET + DATE
    // =========================================================

    @GetMapping("/market/{marketId}/date")
    public ResponseEntity<ExpectedPriceResponseDto>
            getByMarketAndDate(@PathVariable Long marketId,
                    @RequestParam
                    @DateTimeFormat(
                            iso = DateTimeFormat.ISO.DATE
                    )
                    LocalDate date) {

 return ResponseEntity.ok(expectedPriceService.getByMarketAndDate(marketId, date));
    }

    // =========================================================
    // GET ALL ACTIVE
    // =========================================================

    @GetMapping
    public ResponseEntity<List<ExpectedPriceResponseDto>>
            getActiveExpectedPrices() {

 return ResponseEntity.ok(expectedPriceService.getActiveExpectedPrices());
    }

    // =========================================================
    // GET BY DATE RANGE
    // =========================================================

    @GetMapping("/date-range")
    public ResponseEntity<List<ExpectedPriceResponseDto>>
            getByDateRange(@RequestParam @DateTimeFormat( iso = DateTimeFormat.ISO.DATE )
                    LocalDate startDate,

                    @RequestParam
                    @DateTimeFormat(
                            iso = DateTimeFormat.ISO.DATE
                    )
                    LocalDate endDate) {

 return ResponseEntity.ok(expectedPriceService.getByDateRange(startDate, endDate));
    }

    // =========================================================
    // MARKET + DATE RANGE
    // =========================================================

    @GetMapping("/market/{marketId}/date-range")
    public ResponseEntity<List<ExpectedPriceResponseDto>>
            getMarketDateRange(@PathVariable Long marketId,

                    @RequestParam
                    @DateTimeFormat(
                            iso = DateTimeFormat.ISO.DATE
                    )
                    LocalDate startDate,

                    @RequestParam
                    @DateTimeFormat(
                            iso = DateTimeFormat.ISO.DATE
                    )
                    LocalDate endDate) {

 return ResponseEntity.ok(expectedPriceService.getMarketDateRange(marketId, startDate, endDate));
    }

    // =========================================================
    // SOFT DELETE
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
            deleteExpectedPrice(@PathVariable Long id) {

        expectedPriceService.deleteExpectedPrice(id);

 return ResponseEntity.noContent().build();
    }

    // =========================================================
    // COUNT ACTIVE
    // =========================================================

    @GetMapping("/count")
    public ResponseEntity<Long>
            countActiveExpectedPrices() {

 return ResponseEntity.ok(expectedPriceService.countActiveExpectedPrices());
    }
}