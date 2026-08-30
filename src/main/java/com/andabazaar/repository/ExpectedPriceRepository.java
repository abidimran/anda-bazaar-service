package com.andabazaar.repository;

import com.andabazaar.repository.entity.ExpectedPrice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpectedPriceRepository
        extends JpaRepository<ExpectedPrice, Long> {
    List<ExpectedPrice> findByMarketIdOrderByExpectedDateDesc(Long marketId);

    Optional<ExpectedPrice> findByMarketIdAndExpectedDate(Long marketId, LocalDate expectedDate);

    boolean existsByMarketIdAndExpectedDate(Long marketId, LocalDate expectedDate);

    List<ExpectedPrice> findByActiveTrueOrderByExpectedDateDesc();

    List<ExpectedPrice> findByMarketIdAndActiveTrueOrderByExpectedDateDesc(Long marketId);

    List<ExpectedPrice> findByExpectedDateBetweenOrderByExpectedDateDesc(LocalDate startDate, LocalDate endDate);

    List<ExpectedPrice> findByMarketIdAndExpectedDateBetweenOrderByExpectedDateDesc(Long marketId, LocalDate startDate, LocalDate endDate);

    long countByMarketId(Long marketId);

    long countByActiveTrue();

    long countByExpectedDate(LocalDate expectedDate);
}
