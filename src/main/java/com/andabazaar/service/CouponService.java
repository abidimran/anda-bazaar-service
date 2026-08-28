package com.andabazaar.service;

import java.util.List;

import com.andabazaar.dto.coupon.CouponRequestDto;
import com.andabazaar.dto.coupon.CouponResponseDto;

public interface CouponService {

    CouponResponseDto createCoupon(
            CouponRequestDto request);

    CouponResponseDto updateCoupon(
            Long id,
            CouponRequestDto request);

    CouponResponseDto getCouponById(
            Long id);

    CouponResponseDto getCouponByCode(
            String code);

    List<CouponResponseDto> getActiveCoupons();

    List<CouponResponseDto> getAllCoupons();

    void deactivateCoupon(Long id);

    void deleteCoupon(Long id);

    CouponResponseDto applyCoupon(
            String code,
            java.math.BigDecimal orderAmount);

    void expireCoupons();
    
    
}