package com.andabazaar.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.eggprice.EggPriceRequestDto;
import com.andabazaar.dto.eggprice.EggPriceResponseDto;
import com.andabazaar.service.EggPriceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/egg-prices")
@RequiredArgsConstructor
public class EggPriceController {

    private final EggPriceService eggPriceService;

    // =====================================================
    // ADMIN - CREATE PRICE
    // POST /api/egg-prices
    // =====================================================

    @PostMapping
    public ResponseEntity<EggPriceResponseDto> createPrice(
            @Valid @RequestBody EggPriceRequestDto request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        eggPriceService.createPrice(request)
                );
    }

    // =====================================================
    // ADMIN - UPDATE PRICE
    // PUT /api/egg-prices/{id}
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<EggPriceResponseDto> updatePrice(
            @PathVariable Long id,
            @Valid @RequestBody EggPriceRequestDto request) {

        return ResponseEntity.ok(
                eggPriceService.updatePrice(
                        id,
                        request
                )
        );
    }

    // =====================================================
    // GET PRICE BY ID
    // GET /api/egg-prices/{id}
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<EggPriceResponseDto> getPrice(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                eggPriceService.getPriceById(id)
        );
    }

    // =====================================================
    // GET MARKET PRICE BY DATE
    // GET /api/egg-prices/market/{marketId}?date=YYYY-MM-DD
    // =====================================================

    @GetMapping("/market/{marketId}")
    public ResponseEntity<EggPriceResponseDto> getMarketPrice(
            @PathVariable Long marketId,
            @RequestParam LocalDate date) {

        return ResponseEntity.ok(
                eggPriceService.getMarketPrice(
                        marketId,
                        date
                )
        );
    }

    // =====================================================
    // GET TODAY PRICES
    // GET /api/egg-prices/today
    // =====================================================

    @GetMapping("/today")
    public ResponseEntity<List<EggPriceResponseDto>>
            getTodayPrices() {

        return ResponseEntity.ok(
                eggPriceService.getTodayPrices()
        );
    }

    // =====================================================
    // GET YESTERDAY PRICES
    // GET /api/egg-prices/yesterday
    // =====================================================

    @GetMapping("/yesterday")
    public ResponseEntity<List<EggPriceResponseDto>>
            getYesterdayPrices() {

        return ResponseEntity.ok(
                eggPriceService.getYesterdayPrices()
        );
    }

    // =====================================================
    // GET PRICE HISTORY
    // GET /api/egg-prices/history/{marketId}
    // =====================================================

    @GetMapping("/history/{marketId}")
    public ResponseEntity<List<EggPriceResponseDto>>
            getPriceHistory(
                    @PathVariable Long marketId,
                    @RequestParam LocalDate startDate,
                    @RequestParam LocalDate endDate) {

        return ResponseEntity.ok(
                eggPriceService.getPriceHistory(
                        marketId,
                        startDate,
                        endDate
                )
        );
    }

    // =====================================================
    // USER - GET PRICES
    //
    // ACTIVE SUBSCRIPTION:
    // Today + Yesterday + Older
    //
    // NO/EXPIRED SUBSCRIPTION:
    // 2 DAYS OLD + OLDER
    //
    // GET /api/egg-prices/user/{userId}
    // =====================================================

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EggPriceResponseDto>>
            getUserPrices(
                    @PathVariable Long userId) {

        return ResponseEntity.ok(
                eggPriceService.getUserPrices(userId)
        );
    }

    // =====================================================
    // USER - GET PRICE HISTORY
    //
    // GET:
    // /api/egg-prices/user/{userId}/history/{marketId}
    //
    // Example:
    // /api/egg-prices/user/1/history/5
    // ?startDate=2026-08-01
    // &endDate=2026-08-26
    // =====================================================

    @GetMapping("/user/{userId}/history/{marketId}")
    public ResponseEntity<List<EggPriceResponseDto>>
            getUserPriceHistory(
                    @PathVariable Long userId,
                    @PathVariable Long marketId,
                    @RequestParam LocalDate startDate,
                    @RequestParam LocalDate endDate) {

        return ResponseEntity.ok(
                eggPriceService.getUserPriceHistory(
                        userId,
                        marketId,
                        startDate,
                        endDate
                )
        );
    }

    // =====================================================
    // ADMIN - DELETE PRICE
    // DELETE /api/egg-prices/{id}
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrice(
            @PathVariable Long id) {

        eggPriceService.deletePrice(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}