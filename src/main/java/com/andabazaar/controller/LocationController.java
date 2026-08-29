package com.andabazaar.controller;

import com.andabazaar.dto.common.PagedResponse;
import com.andabazaar.dto.location.LocationRequestDto;
import com.andabazaar.dto.location.LocationResponseDto;
import com.andabazaar.service.LocationService;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Locations", description = "Location management with auto-create for country, state, city")
@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @Operation(summary = "Create location")
    @PostMapping
    public ResponseEntity<LocationResponseDto> createLocation(@Valid @RequestBody LocationRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(locationService.createLocation(request));
    }

    @Operation(summary = "Get location by ID")
    @GetMapping("/{id}")
    public ResponseEntity<LocationResponseDto> getLocation(@PathVariable Long id) {
        return ResponseEntity.ok(locationService.getLocationById(id));
    }

    @Operation(summary = "Get all locations")
    @GetMapping
    public ResponseEntity<PagedResponse<LocationResponseDto>> getAllLocations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(PagedResponse.fromList(locationService.getAllLocations(), page, size));
    }

    @Operation(summary = "Update location")
    @PutMapping("/{id}")
    public ResponseEntity<LocationResponseDto> updateLocation(@PathVariable Long id, @Valid @RequestBody LocationRequestDto request) {
        return ResponseEntity.ok(locationService.updateLocation(id, request));
    }

    @Operation(summary = "Delete location")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get RapidAPI-enabled locations")
    @GetMapping("/rapid-enabled")
    public ResponseEntity<PagedResponse<LocationResponseDto>> getRapidEnabledLocations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(PagedResponse.fromList(locationService.getRapidEnabledLocations(), page, size));
    }
}
