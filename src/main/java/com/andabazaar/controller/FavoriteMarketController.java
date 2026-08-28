package com.andabazaar.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.entity.FavoriteMarket;
import com.andabazaar.service.FavoriteMarketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteMarketController {

    private final FavoriteMarketService favoriteMarketService;

    // Add favorite market
    @PostMapping("/{userId}/{marketId}")
    public ResponseEntity<FavoriteMarket> addFavorite(@PathVariable Long userId, @PathVariable Long marketId) {

 return ResponseEntity.status(HttpStatus.CREATED).body(favoriteMarketService.addFavorite(userId, marketId));
    }

    // Remove favorite market
    @DeleteMapping("/{userId}/{marketId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long userId, @PathVariable Long marketId) {

        favoriteMarketService.removeFavorite( userId, marketId);

 return ResponseEntity.noContent().build();
    }

    // Get user's favorite markets
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FavoriteMarket>> getUserFavorites(@PathVariable Long userId) {

 return ResponseEntity.ok(favoriteMarketService.getUserFavorites(userId));
    }

    // Check favorite
    @GetMapping("/check/{userId}/{marketId}")
    public ResponseEntity<Boolean> isFavorite(@PathVariable Long userId, @PathVariable Long marketId) {

 return ResponseEntity.ok(favoriteMarketService.isFavorite(userId, marketId));
    }

    // Count favorites
    @GetMapping("/count/{userId}")
    public ResponseEntity<Long> countFavorites(@PathVariable Long userId) {

 return ResponseEntity.ok(favoriteMarketService.countUserFavorites(userId));
    }
}