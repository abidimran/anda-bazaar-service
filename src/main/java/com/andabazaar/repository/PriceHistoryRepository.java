package com.andabazaar.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andabazaar.entity.PriceHistory;

public interface PriceHistoryRepository
        extends JpaRepository<PriceHistory, Long> {

    List<PriceHistory>
    findByMarketIdOrderByPriceDateDesc( Long marketId);

    List<PriceHistory>
    findByMarketIdAndPriceDateBetweenOrderByPriceDateDesc( Long marketId, LocalDate startDate, LocalDate endDate);

    List<PriceHistory>
    findByPriceDateOrderByPriceDateDesc( LocalDate priceDate);

    boolean existsByMarketIdAndPriceDate( Long marketId, LocalDate priceDate);

    long countByMarketId( Long marketId);
}