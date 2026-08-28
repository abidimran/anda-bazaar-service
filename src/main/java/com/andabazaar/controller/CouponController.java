package com.andabazaar.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.coupon.CouponRequestDto;
import com.andabazaar.dto.coupon.CouponResponseDto;
import com.andabazaar.service.CouponService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<CouponResponseDto> createCoupon(@Valid @RequestBody CouponRequestDto request) {

 return ResponseEntity.status(HttpStatus.CREATED).body(couponService.createCoupon(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CouponResponseDto> updateCoupon(@PathVariable Long id, @Valid @RequestBody CouponRequestDto request) {

 return ResponseEntity.ok(couponService.updateCoupon(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponResponseDto> getCouponById(@PathVariable Long id) {

 return ResponseEntity.ok(couponService.getCouponById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CouponResponseDto> getCouponByCode(@PathVariable String code) {

 return ResponseEntity.ok(couponService.getCouponByCode(code));
    }

    @GetMapping
    public ResponseEntity<List<CouponResponseDto>> getAllCoupons() {

 return ResponseEntity.ok(couponService.getAllCoupons());
    }

    @GetMapping("/active")
    public ResponseEntity<List<CouponResponseDto>> getActiveCoupons() {

 return ResponseEntity.ok(couponService.getActiveCoupons());
    }

    @GetMapping("/apply")
    public ResponseEntity<CouponResponseDto> applyCoupon(@RequestParam String code, @RequestParam BigDecimal orderAmount) {

 return ResponseEntity.ok(couponService.applyCoupon(code, orderAmount));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateCoupon(@PathVariable Long id) {

        couponService.deactivateCoupon(id);

 return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {

        couponService.deleteCoupon(id);

 return ResponseEntity.noContent().build();
    }
}