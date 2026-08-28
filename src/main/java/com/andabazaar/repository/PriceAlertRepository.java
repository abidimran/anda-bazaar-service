package com.andabazaar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andabazaar.entity.PriceAlert;

public interface PriceAlertRepository
        extends JpaRepository<PriceAlert, Long> {

    // =========================================================
    // GET ALL ALERTS OF USER
    // =========================================================

    List<PriceAlert> findByUserIdOrderByCreatedAtDesc(
            Long userId
    );

    // =========================================================
    // GET ACTIVE ALERTS OF USER
    // =========================================================

    List<PriceAlert> findByUserIdAndActiveTrueOrderByCreatedAtDesc(
            Long userId
    );

    // =========================================================
    // COUNT ALL ALERTS OF USER
    // =========================================================

    long countByUserId(
            Long userId
    );

    // =========================================================
    // COUNT ACTIVE ALERTS OF USER
    // =========================================================

    long countByUserIdAndActiveTrue(
            Long userId
    );
}