package com.andabazaar.controller;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.common.PagedResponse;
import com.andabazaar.dto.coupon.CouponRequestDto;
import com.andabazaar.dto.coupon.CouponResponseDto;
import com.andabazaar.service.CouponService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Coupons", description = "Coupon and discount management")
@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @Operation(summary = "Create Coupon")
    @PostMapping
    public ResponseEntity<CouponResponseDto> createCoupon(@Valid @RequestBody CouponRequestDto request) {

 return ResponseEntity.status(HttpStatus.CREATED).body(couponService.createCoupon(request));
    }

    @Operation(summary = "Update Coupon")
    @PutMapping("/{id}")
    public ResponseEntity<CouponResponseDto> updateCoupon(@PathVariable Long id, @Valid @RequestBody CouponRequestDto request) {

 return ResponseEntity.ok(couponService.updateCoupon(id, request));
    }

    @Operation(summary = "Get Coupon By Id")
    @GetMapping("/{id}")
    public ResponseEntity<CouponResponseDto> getCouponById(@PathVariable Long id) {

 return ResponseEntity.ok(couponService.getCouponById(id));
    }

    @Operation(summary = "Get Coupon By Code")
    @GetMapping("/code/{code}")
    public ResponseEntity<CouponResponseDto> getCouponByCode(@PathVariable String code) {

 return ResponseEntity.ok(couponService.getCouponByCode(code));
    }

    @Operation(summary = "Get All Coupons")
    @GetMapping
    public ResponseEntity<PagedResponse<CouponResponseDto>>
            getAllCoupons(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(couponService.getAllCoupons(), page, size));
    }

    @Operation(summary = "Get Active Coupons")
    @GetMapping("/active")
    public ResponseEntity<PagedResponse<CouponResponseDto>>
            getActiveCoupons(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(couponService.getActiveCoupons(), page, size));
    }

    @Operation(summary = "Apply Coupon")
    @GetMapping("/validation")
    public ResponseEntity<CouponResponseDto> applyCoupon(@RequestParam String code, @RequestParam BigDecimal orderAmount) {

 return ResponseEntity.ok(couponService.applyCoupon(code, orderAmount));
    }

    @Operation(summary = "Deactivate Coupon")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateCoupon(@PathVariable Long id) {

        couponService.deactivateCoupon(id);

 return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete Coupon")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {

        couponService.deleteCoupon(id);

 return ResponseEntity.noContent().build();
    }
}
