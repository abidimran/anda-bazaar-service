
package com.andabazaar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andabazaar.entity.CouponUsage;

public interface CouponUsageRepository
        extends JpaRepository<CouponUsage, Long> {

    List<CouponUsage> findByUserIdOrderByUsedAtDesc( Long userId);

    List<CouponUsage> findByCouponIdOrderByUsedAtDesc( Long couponId);

    List<CouponUsage> findByUserIdAndCouponIdOrderByUsedAtDesc( Long userId, Long couponId);

    long countByUserId( Long userId);

    long countByCouponId( Long couponId);

    // Check whether a user already used a coupon
    boolean existsByCouponIdAndUserId( Long couponId, Long userId);
}
