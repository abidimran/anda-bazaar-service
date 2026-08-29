package com.andabazaar.controller;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.expectedprice.ExpectedPriceRequestDto;
import com.andabazaar.dto.expectedprice.ExpectedPriceResponseDto;
import com.andabazaar.service.ExpectedPriceService;

import lombok.RequiredArgsConstructor;

@Tag(name = "Expected Prices", description = "Expected price predictions")
@RestController
@RequestMapping("/api/expected-prices")
@RequiredArgsConstructor
public class ExpectedPriceController {

    private final ExpectedPriceService expectedPriceService;

    @Operation(summary = "Create Expected Price")
    @PostMapping
    public ResponseEntity<ExpectedPriceResponseDto>
            createExpectedPrice(@RequestBody ExpectedPriceRequestDto request) {

 return ResponseEntity.ok(expectedPriceService.createExpectedPrice(request));
    }

    @Operation(summary = "Update Expected Price")
    @PutMapping("/{id}")
    public ResponseEntity<ExpectedPriceResponseDto>
            updateExpectedPrice(@PathVariable Long id, @RequestBody ExpectedPriceRequestDto request) {

 return ResponseEntity.ok(expectedPriceService.updateExpectedPrice(id, request));
    }

    @Operation(summary = "Get Expected Price By Id")
    @GetMapping("/{id}")
    public ResponseEntity<ExpectedPriceResponseDto>
            getExpectedPriceById(@PathVariable Long id) {

 return ResponseEntity.ok(expectedPriceService.getExpectedPriceById(id));
    }

    @Operation(summary = "Get By Market")
    @GetMapping("/market/{marketId}")
    public ResponseEntity<List<ExpectedPriceResponseDto>>
            getByMarket(@PathVariable Long marketId) {

 return ResponseEntity.ok(expectedPriceService.getByMarket(marketId));
    }

    @Operation(summary = "Get By Market And Date")
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

    @Operation(summary = "Get Active Expected Prices")
    @GetMapping
    public ResponseEntity<List<ExpectedPriceResponseDto>>
            getActiveExpectedPrices() {

 return ResponseEntity.ok(expectedPriceService.getActiveExpectedPrices());
    }

    @Operation(summary = "Get By Date Range")
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

    @Operation(summary = "Get Market Date Range")
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

    @Operation(summary = "Delete Expected Price")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
            deleteExpectedPrice(@PathVariable Long id) {

        expectedPriceService.deleteExpectedPrice(id);

 return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Count Active Expected Prices")
    @GetMapping("/count")
    public ResponseEntity<Long>
            countActiveExpectedPrices() {

 return ResponseEntity.ok(expectedPriceService.countActiveExpectedPrices());
    }
}