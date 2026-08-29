package com.andabazaar.controller;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andabazaar.service.LocationSyncService;

import lombok.RequiredArgsConstructor;

@Tag(name = "Location Sync", description = "Sync states and cities from external API")
@RestController
@RequestMapping("/api/locations/sync")
@RequiredArgsConstructor
public class LocationSyncController {

    private final LocationSyncService locationSyncService;

    @Operation(summary = "Sync States And Cities")
    @PostMapping
    public ResponseEntity<Map<String, String>> syncStatesAndCities() {
        locationSyncService.syncStatesAndCities();
        return ResponseEntity.ok(Map.of("message", "States and cities sync completed"));
    }
}
