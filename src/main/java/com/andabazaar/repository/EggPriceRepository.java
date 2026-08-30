package com.andabazaar.repository;

import com.andabazaar.repository.entity.EggPrice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EggPriceRepository
        extends JpaRepository<EggPrice, Long> {
    List<EggPrice> findByPriceDateOrderByPriceDateDesc(LocalDate priceDate);

    long countByPriceDate(LocalDate priceDate);

    List<EggPrice> findByMarketIdOrderByPriceDateDesc(Long marketId);

    Optional<EggPrice> findByMarketIdAndPriceDate(Long marketId, LocalDate priceDate);

    List<EggPrice> findByMarketIdAndPriceDateBetweenOrderByPriceDateDesc(Long marketId, LocalDate startDate, LocalDate endDate);

    boolean existsByMarketIdAndPriceDate(Long marketId, LocalDate priceDate);

    List<EggPrice> findByPriceDateBetweenOrderByPriceDateDesc(LocalDate startDate, LocalDate endDate);
}
