package com.andabazaar.controller;

import com.andabazaar.dto.common.PagedResponse;
import com.andabazaar.dto.expectedprice.ExpectedPriceRequestDto;
import com.andabazaar.dto.expectedprice.ExpectedPriceResponseDto;
import com.andabazaar.service.ExpectedPriceService;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<PagedResponse<ExpectedPriceResponseDto>>
            getByMarket(@PathVariable Long marketId,
                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
 return ResponseEntity.ok(PagedResponse.fromList(expectedPriceService.getByMarket(marketId), page, size));
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
    public ResponseEntity<PagedResponse<ExpectedPriceResponseDto>>
            getActiveExpectedPrices(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
 return ResponseEntity.ok(PagedResponse.fromList(expectedPriceService.getActiveExpectedPrices(), page, size));
    }

    @Operation(summary = "Get By Date Range")
    @GetMapping("/date-range")
    public ResponseEntity<PagedResponse<ExpectedPriceResponseDto>>
            getByDateRange(@RequestParam @DateTimeFormat( iso = DateTimeFormat.ISO.DATE )
                    LocalDate startDate,
                    @RequestParam
                    @DateTimeFormat(
                            iso = DateTimeFormat.ISO.DATE
                    )
                    LocalDate endDate,
                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
 return ResponseEntity.ok(PagedResponse.fromList(expectedPriceService.getByDateRange(startDate, endDate), page, size));
    }

    @Operation(summary = "Get Market Date Range")
    @GetMapping("/market/{marketId}/date-range")
    public ResponseEntity<PagedResponse<ExpectedPriceResponseDto>>
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
                    LocalDate endDate,
                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
 return ResponseEntity.ok(PagedResponse.fromList(expectedPriceService.getMarketDateRange(marketId, startDate, endDate), page, size));
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
