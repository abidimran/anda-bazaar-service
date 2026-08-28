package com.andabazaar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andabazaar.entity.PriceReport;

public interface PriceReportRepository
        extends JpaRepository<PriceReport, Long> {

    // =========================================================
    // ALL REPORTS
    // =========================================================

    List<PriceReport>
    findAllByOrderByCreatedAtDesc();

    // =========================================================
    // USER REPORTS
    // =========================================================

    List<PriceReport>
    findByUserIdOrderByCreatedAtDesc(
            Long userId
    );

    // =========================================================
    // MARKET REPORTS
    // =========================================================

    List<PriceReport>
    findByMarketIdOrderByCreatedAtDesc(
            Long marketId
    );

    // =========================================================
    // STATUS
    // =========================================================

    List<PriceReport>
    findByStatusOrderByCreatedAtDesc(
            String status
    );

    // =========================================================
    // REVIEWED
    // =========================================================

    List<PriceReport>
    findByReviewedOrderByCreatedAtDesc(
            Boolean reviewed
    );

    // =========================================================
    // PENDING COUNT
    // =========================================================

    long countByStatus(
            String status
    );

    // =========================================================
    // MARKET COUNT
    // =========================================================

    long countByMarketId(
            Long marketId
    );

    // =========================================================
    // USER COUNT
    // =========================================================

    long countByUserId(
            Long userId
    );
}