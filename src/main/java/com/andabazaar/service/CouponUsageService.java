package com.andabazaar.service;

import java.util.List;

import com.andabazaar.entity.CouponUsage;

public interface CouponUsageService {

    // =========================
    // RECORD COUPON USAGE
    // =========================

    CouponUsage recordUsage(
            Long couponId,
            Long userId
    );

    // =========================
    // GET USAGE BY ID
    // =========================

    CouponUsage getUsageById(
            Long id
    );

    // =========================
    // GET ALL USAGE
    // =========================

    List<CouponUsage> getAllUsage();

    // =========================
    // GET USER USAGE
    // =========================

    List<CouponUsage> getUserUsage(
            Long userId
    );

    // =========================
    // GET COUPON USAGE
    // =========================

    List<CouponUsage> getCouponUsage(
            Long couponId
    );

    // =========================
    // GET USER + COUPON USAGE
    // =========================

    List<CouponUsage> getUserCouponUsage(
            Long userId,
            Long couponId
    );

    // =========================
    // COUNT USER USAGE
    // =========================

    long countUserUsage(
            Long userId
    );

    // =========================
    // COUNT COUPON USAGE
    // =========================

    long countCouponUsage(
            Long couponId
    );
}