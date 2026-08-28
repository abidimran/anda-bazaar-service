package com.andabazaar.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andabazaar.entity.ExpectedPrice;

public interface ExpectedPriceRepository
        extends JpaRepository<ExpectedPrice, Long> {

    // =========================
    // FIND BY MARKET
    // =========================

    List<ExpectedPrice>
    findByMarketIdOrderByExpectedDateDesc(
            Long marketId
    );

    // =========================
    // FIND BY MARKET + DATE
    // =========================

    Optional<ExpectedPrice>
    findByMarketIdAndExpectedDate(
            Long marketId,
            LocalDate expectedDate
    );

    // =========================
    // CHECK DUPLICATE
    // =========================

    boolean existsByMarketIdAndExpectedDate(
            Long marketId,
            LocalDate expectedDate
    );

    // =========================
    // ACTIVE EXPECTED PRICES
    // =========================

    List<ExpectedPrice>
    findByActiveTrueOrderByExpectedDateDesc();

    // =========================
    // ACTIVE BY MARKET
    // =========================

    List<ExpectedPrice>
    findByMarketIdAndActiveTrueOrderByExpectedDateDesc(
            Long marketId
    );

    // =========================
    // DATE RANGE
    // =========================

    List<ExpectedPrice>
    findByExpectedDateBetweenOrderByExpectedDateDesc(
            LocalDate startDate,
            LocalDate endDate
    );

    // =========================
    // MARKET + DATE RANGE
    // =========================

    List<ExpectedPrice>
    findByMarketIdAndExpectedDateBetweenOrderByExpectedDateDesc(
            Long marketId,
            LocalDate startDate,
            LocalDate endDate
    );

    // =========================
    // COUNT
    // =========================

    long countByMarketId(
            Long marketId
    );

    long countByActiveTrue();

    long countByExpectedDate(
            LocalDate expectedDate
    );
}