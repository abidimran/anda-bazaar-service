package com.andabazaar.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.entity.FavoriteMarket;
import com.andabazaar.service.FavoriteMarketService;

import lombok.RequiredArgsConstructor;

@Tag(name = "Favorite Markets", description = "User favorite market management")
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteMarketController {

    private final FavoriteMarketService favoriteMarketService;

    // Add favorite market
    @Operation(summary = "Add Favorite")
    @PostMapping("/{userId}/{marketId}")
    public ResponseEntity<FavoriteMarket> addFavorite(@PathVariable Long userId, @PathVariable Long marketId) {

 return ResponseEntity.status(HttpStatus.CREATED).body(favoriteMarketService.addFavorite(userId, marketId));
    }

    // Remove favorite market
    @Operation(summary = "Remove Favorite")
    @DeleteMapping("/{userId}/{marketId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long userId, @PathVariable Long marketId) {

        favoriteMarketService.removeFavorite( userId, marketId);

 return ResponseEntity.noContent().build();
    }

    // Get user's favorite markets
    @Operation(summary = "Get User Favorites")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FavoriteMarket>> getUserFavorites(@PathVariable Long userId) {

 return ResponseEntity.ok(favoriteMarketService.getUserFavorites(userId));
    }

    // Check favorite
    @Operation(summary = "Is Favorite")
    @GetMapping("/{userId}/{marketId}/exists")
    public ResponseEntity<Boolean> isFavorite(@PathVariable Long userId, @PathVariable Long marketId) {

 return ResponseEntity.ok(favoriteMarketService.isFavorite(userId, marketId));
    }

    // Count favorites
    @Operation(summary = "Count Favorites")
    @GetMapping("/count/{userId}")
    public ResponseEntity<Long> countFavorites(@PathVariable Long userId) {

 return ResponseEntity.ok(favoriteMarketService.countUserFavorites(userId));
    }
}