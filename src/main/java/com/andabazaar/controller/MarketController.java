package com.andabazaar.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.market.MarketRequestDto;
import com.andabazaar.dto.market.MarketResponseDto;
import com.andabazaar.service.MarketService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/markets")
@RequiredArgsConstructor
public class MarketController {

    private final MarketService marketService;

    @PostMapping
    public ResponseEntity<MarketResponseDto> createMarket(@Valid @RequestBody MarketRequestDto request) {

 return ResponseEntity.status(HttpStatus.CREATED).body(marketService.createMarket(request));
    }

    @GetMapping
    public ResponseEntity<List<MarketResponseDto>>
            getAllMarkets() {

 return ResponseEntity.ok(marketService.getAllMarkets());
    }

    @GetMapping("/city/{cityId}")
    public ResponseEntity<List<MarketResponseDto>>
            getMarketsByCity(@PathVariable Long cityId) {

 return ResponseEntity.ok(marketService.getMarketsByCity(cityId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarketResponseDto> getMarket(@PathVariable Long id) {

 return ResponseEntity.ok(marketService.getMarketById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarketResponseDto> updateMarket(@PathVariable Long id, @Valid @RequestBody MarketRequestDto request) {

 return ResponseEntity.ok(marketService.updateMarket(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarket(@PathVariable Long id) {

        marketService.deleteMarket(id);

 return ResponseEntity.noContent().build();
    }
}