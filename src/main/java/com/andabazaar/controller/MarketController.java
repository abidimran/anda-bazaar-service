package com.andabazaar.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.market.MarketRequestDto;
import com.andabazaar.dto.market.MarketResponseDto;
import com.andabazaar.service.MarketService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Markets", description = "Market management")
@RestController
@RequestMapping("/api/markets")
@RequiredArgsConstructor
public class MarketController {

    private final MarketService marketService;

    @Operation(summary = "Create Market")
    @PostMapping
    public ResponseEntity<MarketResponseDto> createMarket(@Valid @RequestBody MarketRequestDto request) {

 return ResponseEntity.status(HttpStatus.CREATED).body(marketService.createMarket(request));
    }

    @Operation(summary = "Get All Markets")
    @GetMapping
    public ResponseEntity<List<MarketResponseDto>>
            getAllMarkets() {

 return ResponseEntity.ok(marketService.getAllMarkets());
    }

    @Operation(summary = "Get Markets By City")
    @GetMapping("/city/{cityId}")
    public ResponseEntity<List<MarketResponseDto>>
            getMarketsByCity(@PathVariable Long cityId) {

 return ResponseEntity.ok(marketService.getMarketsByCity(cityId));
    }

    @Operation(summary = "Get Market")
    @GetMapping("/{id}")
    public ResponseEntity<MarketResponseDto> getMarket(@PathVariable Long id) {

 return ResponseEntity.ok(marketService.getMarketById(id));
    }

    @Operation(summary = "Update Market")
    @PutMapping("/{id}")
    public ResponseEntity<MarketResponseDto> updateMarket(@PathVariable Long id, @Valid @RequestBody MarketRequestDto request) {

 return ResponseEntity.ok(marketService.updateMarket(id, request));
    }

    @Operation(summary = "Delete Market")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarket(@PathVariable Long id) {

        marketService.deleteMarket(id);

 return ResponseEntity.noContent().build();
    }
}