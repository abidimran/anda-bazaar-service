package com.andabazaar.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andabazaar.entity.Coupon;
import com.andabazaar.enums.CouponStatus;

public interface CouponRepository
        extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);

    List<Coupon> findByActiveTrueOrderByCreatedAtDesc();

    List<Coupon> findByStatusOrderByCreatedAtDesc( CouponStatus status);

    List<Coupon> findByStatusAndEndDateBefore( CouponStatus status, LocalDateTime date);

    List<Coupon>
    findByActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual( LocalDateTime startDate, LocalDateTime endDate);

    long countByStatus(CouponStatus status);
}