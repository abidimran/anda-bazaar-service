package com.andabazaar.controller;

import com.andabazaar.dto.common.PagedResponse;
import com.andabazaar.dto.eggprice.EggPriceRequestDto;
import com.andabazaar.dto.eggprice.EggPriceResponseDto;
import com.andabazaar.service.EggPriceService;

import jakarta.validation.Valid;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Egg Prices", description = "Egg price CRUD and user price access")
@RestController
@RequestMapping("/api/egg-prices")
@RequiredArgsConstructor
public class EggPriceController {
    private final EggPriceService eggPriceService;
    // ADMIN - CREATE PRICE
    @Operation(summary = "Create Price")
    @PostMapping
    public ResponseEntity<EggPriceResponseDto> createPrice(@Valid @RequestBody EggPriceRequestDto request) {
 return ResponseEntity.status(HttpStatus.CREATED).body(eggPriceService.createPrice(request));
    }

    // ADMIN - UPDATE PRICE
    @Operation(summary = "Update Price")
    @PutMapping("/{id}")
    public ResponseEntity<EggPriceResponseDto> updatePrice(@PathVariable Long id,
            @Valid @RequestBody EggPriceRequestDto request) {
 return ResponseEntity.ok(eggPriceService.updatePrice(id, request));
    }

    // GET PRICE BY ID
    @Operation(summary = "Get Price")
    @GetMapping("/{id}")
    public ResponseEntity<EggPriceResponseDto> getPrice(@PathVariable Long id) {
 return ResponseEntity.ok(eggPriceService.getPriceById(id));
    }

    // GET MARKET PRICE BY DATE
    @Operation(summary = "Get Market Price")
    @GetMapping("/market/{marketId}")
    public ResponseEntity<EggPriceResponseDto> getMarketPrice(@PathVariable Long marketId, @RequestParam LocalDate date) {
 return ResponseEntity.ok(eggPriceService.getMarketPrice(marketId, date));
    }

    // GET TODAY PRICES
    @Operation(summary = "Get Today Prices")
    @GetMapping("/today")
    public ResponseEntity<PagedResponse<EggPriceResponseDto>>
            getTodayPrices(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
 return ResponseEntity.ok(PagedResponse.fromList(eggPriceService.getTodayPrices(), page, size));
    }

    // GET YESTERDAY PRICES
    @Operation(summary = "Get Yesterday Prices")
    @GetMapping("/yesterday")
    public ResponseEntity<PagedResponse<EggPriceResponseDto>>
            getYesterdayPrices(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
 return ResponseEntity.ok(PagedResponse.fromList(eggPriceService.getYesterdayPrices(), page, size));
    }

    // GET PRICE HISTORY
    @Operation(summary = "Get Price History")
    @GetMapping("/history/{marketId}")
    public ResponseEntity<PagedResponse<EggPriceResponseDto>>
            getPriceHistory(@PathVariable Long marketId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate,
                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
 return ResponseEntity.ok(PagedResponse.fromList(eggPriceService.getPriceHistory(marketId, startDate, endDate), page, size));
    }

    // USER - GET PRICES
    //
    // ACTIVE SUBSCRIPTION:
    // Today + Yesterday + Older
    //
    // NO/EXPIRED SUBSCRIPTION:
    // 2 DAYS OLD + OLDER
    //
    @Operation(summary = "Get User Prices")
    @GetMapping("/user/{userId}")
    public ResponseEntity<PagedResponse<EggPriceResponseDto>>
            getUserPrices(@PathVariable Long userId,
                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
 return ResponseEntity.ok(PagedResponse.fromList(eggPriceService.getUserPrices(userId), page, size));
    }

    // USER - GET PRICE HISTORY
    //
    // GET:
    // /api/egg-prices/user/{userId}/history/{marketId}
    //
    // Example:
    // /api/egg-prices/user/1/history/5
    // ?startDate=2026-08-01
    @Operation(summary = "Get User Price History")
    @GetMapping("/user/{userId}/history/{marketId}")
    public ResponseEntity<PagedResponse<EggPriceResponseDto>>
            getUserPriceHistory(@PathVariable Long userId,
                    @PathVariable Long marketId,
                    @RequestParam LocalDate startDate,
                    @RequestParam LocalDate endDate,
                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
 return ResponseEntity.ok(PagedResponse.fromList(eggPriceService.getUserPriceHistory(userId, marketId, startDate, endDate), page, size));
    }

    // ADMIN - DELETE PRICE
    @Operation(summary = "Delete Price")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrice(@PathVariable Long id) {
        eggPriceService.deletePrice(id);
 return ResponseEntity.noContent().build();
    }
}
