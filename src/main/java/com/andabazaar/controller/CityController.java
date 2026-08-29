package com.andabazaar.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.common.PagedResponse;
import com.andabazaar.dto.location.CityRequestDto;
import com.andabazaar.dto.location.CityResponseDto;
import com.andabazaar.service.CityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Cities", description = "City management")
@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @Operation(summary = "Create City")
    @PostMapping
    public ResponseEntity<CityResponseDto> createCity(@Valid @RequestBody CityRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cityService.createCity(request));
    }

    @Operation(summary = "Get All Cities")
    @GetMapping
    public ResponseEntity<PagedResponse<CityResponseDto>>
            getAllCities(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(PagedResponse.fromList(cityService.getAllCities(), page, size));
    }

    @Operation(summary = "Get City")
    @GetMapping("/{id}")
    public ResponseEntity<CityResponseDto> getCity(@PathVariable Long id) {
        return ResponseEntity.ok(cityService.getCityById(id));
    }

    @Operation(summary = "Update City")
    @PutMapping("/{id}")
    public ResponseEntity<CityResponseDto> updateCity(@PathVariable Long id, @Valid @RequestBody CityRequestDto request) {
        return ResponseEntity.ok(cityService.updateCity(id, request));
    }

    @Operation(summary = "Delete City")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
        return ResponseEntity.noContent().build();
    }
}
