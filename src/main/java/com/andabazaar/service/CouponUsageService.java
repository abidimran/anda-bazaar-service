package com.andabazaar.service;

import java.util.List;

import com.andabazaar.entity.CouponUsage;

public interface CouponUsageService {

    CouponUsage recordUsage( Long couponId, Long userId);

    CouponUsage getUsageById( Long id);

    List<CouponUsage> getAllUsage();

    List<CouponUsage> getUserUsage( Long userId);

    List<CouponUsage> getCouponUsage( Long couponId);

    List<CouponUsage> getUserCouponUsage( Long userId, Long couponId);

    long countUserUsage( Long userId);

    long countCouponUsage( Long couponId);
}