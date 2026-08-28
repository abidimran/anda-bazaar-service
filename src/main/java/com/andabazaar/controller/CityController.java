package com.andabazaar.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.location.CityRequestDto;
import com.andabazaar.dto.location.CityResponseDto;
import com.andabazaar.service.CityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @PostMapping
    public ResponseEntity<CityResponseDto> createCity(@Valid @RequestBody CityRequestDto request) {

 return ResponseEntity.status(HttpStatus.CREATED).body(cityService.createCity(request));
    }

    @GetMapping
    public ResponseEntity<List<CityResponseDto>>
            getAllCities() {

 return ResponseEntity.ok(cityService.getAllCities());
    }

    @GetMapping("/state/{stateId}")
    public ResponseEntity<List<CityResponseDto>>
            getCitiesByState(@PathVariable Long stateId) {

 return ResponseEntity.ok(cityService.getCitiesByState(stateId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityResponseDto> getCity(@PathVariable Long id) {

 return ResponseEntity.ok(cityService.getCityById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityResponseDto> updateCity(@PathVariable Long id, @Valid @RequestBody CityRequestDto request) {

 return ResponseEntity.ok(cityService.updateCity(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {

        cityService.deleteCity(id);

 return ResponseEntity.noContent().build();
    }
}