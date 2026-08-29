package com.andabazaar.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.alert.PriceAlertRequestDto;
import com.andabazaar.dto.alert.PriceAlertResponseDto;
import com.andabazaar.dto.common.PagedResponse;
import com.andabazaar.service.PriceAlertService;

import lombok.RequiredArgsConstructor;

@Tag(name = "Price Alerts", description = "User price alert configuration")
@RestController
@RequestMapping("/api/price-alerts")
@RequiredArgsConstructor
public class PriceAlertController {

    private final PriceAlertService priceAlertService;

    @Operation(summary = "Create Alert")
    @PostMapping
    public ResponseEntity<PriceAlertResponseDto> createAlert(@RequestBody PriceAlertRequestDto request) {

 return ResponseEntity.ok(priceAlertService.createAlert(request));
    }

    @Operation(summary = "Get Alert By Id")
    @GetMapping("/{id}")
    public ResponseEntity<PriceAlertResponseDto> getAlertById(@PathVariable Long id) {

 return ResponseEntity.ok(priceAlertService.getAlertById(id));
    }

    @Operation(summary = "Get User Alerts")
    @GetMapping("/user/{userId}")
    public ResponseEntity<PagedResponse<PriceAlertResponseDto>>
            getUserAlerts(@PathVariable Long userId,
                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(priceAlertService.getUserAlerts(userId), page, size));
    }

    @Operation(summary = "Update Alert")
    @PutMapping("/{id}")
    public ResponseEntity<PriceAlertResponseDto> updateAlert(@PathVariable Long id, @RequestBody PriceAlertRequestDto request) {

 return ResponseEntity.ok(priceAlertService.updateAlert(id, request));
    }

    @Operation(summary = "Delete Alert")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlert(@PathVariable Long id) {

        priceAlertService.deleteAlert(id);

 return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Toggle Alert")
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<PriceAlertResponseDto> toggleAlert(@PathVariable Long id) {

 return ResponseEntity.ok(priceAlertService.toggleAlert(id));
    }
}
