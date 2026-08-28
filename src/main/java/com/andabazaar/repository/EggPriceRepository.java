package com.andabazaar.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andabazaar.entity.EggPrice;

public interface EggPriceRepository
        extends JpaRepository<EggPrice, Long> {

    // =========================
    // PRICE BY DATE
    // =========================

    List<EggPrice> findByPriceDateOrderByPriceDateDesc(
            LocalDate priceDate
    );

    // =========================
    // DASHBOARD - PRICE COUNT
    // =========================

    long countByPriceDate(
            LocalDate priceDate
    );

    // =========================
    // MARKET PRICE HISTORY
    // =========================

    List<EggPrice> findByMarketIdOrderByPriceDateDesc(
            Long marketId
    );

    // =========================
    // MARKET + DATE
    // =========================

    Optional<EggPrice> findByMarketIdAndPriceDate(
            Long marketId,
            LocalDate priceDate
    );

    // =========================
    // PRICE HISTORY
    // =========================

    List<EggPrice>
    findByMarketIdAndPriceDateBetweenOrderByPriceDateDesc(
            Long marketId,
            LocalDate startDate,
            LocalDate endDate
    );

    // =========================
    // CHECK DUPLICATE PRICE
    // =========================

    boolean existsByMarketIdAndPriceDate(
            Long marketId,
            LocalDate priceDate
    );

    // =========================
    // ALL PRICES BY DATE RANGE
    // =========================

    List<EggPrice>
    findByPriceDateBetweenOrderByPriceDateDesc(
            LocalDate startDate,
            LocalDate endDate
    );
}