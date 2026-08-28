package com.andabazaar.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.alert.PriceAlertRequestDto;
import com.andabazaar.dto.alert.PriceAlertResponseDto;
import com.andabazaar.service.PriceAlertService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/price-alerts")
@RequiredArgsConstructor
public class PriceAlertController {

    private final PriceAlertService priceAlertService;

    // =========================================================
    // CREATE ALERT
    // =========================================================

    @PostMapping
    public ResponseEntity<PriceAlertResponseDto> createAlert(@RequestBody PriceAlertRequestDto request) {

 return ResponseEntity.ok(priceAlertService.createAlert(request));
    }

    // =========================================================
    // GET ALERT BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<PriceAlertResponseDto> getAlertById(@PathVariable Long id) {

 return ResponseEntity.ok(priceAlertService.getAlertById(id));
    }

    // =========================================================
    // GET USER ALERTS
    // =========================================================

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PriceAlertResponseDto>> getUserAlerts(@PathVariable Long userId) {

 return ResponseEntity.ok(priceAlertService.getUserAlerts(userId));
    }

    // =========================================================
    // UPDATE ALERT
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<PriceAlertResponseDto> updateAlert(@PathVariable Long id, @RequestBody PriceAlertRequestDto request) {

 return ResponseEntity.ok(priceAlertService.updateAlert(id, request));
    }

    // =========================================================
    // DELETE ALERT
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlert(@PathVariable Long id) {

        priceAlertService.deleteAlert(id);

 return ResponseEntity.noContent().build();
    }

    // =========================================================
    // TOGGLE ALERT
    // =========================================================

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<PriceAlertResponseDto> toggleAlert(@PathVariable Long id) {

 return ResponseEntity.ok(priceAlertService.toggleAlert(id));
    }
}