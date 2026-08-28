package com.andabazaar.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andabazaar.service.LocationSyncService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/location-sync")
@RequiredArgsConstructor
public class LocationSyncController {

    private final LocationSyncService locationSyncService;

    @PostMapping
    public ResponseEntity<Map<String, String>> syncStatesAndCities() {
        locationSyncService.syncStatesAndCities();
        return ResponseEntity.ok(Map.of("message", "States and cities sync completed"));
    }
}
