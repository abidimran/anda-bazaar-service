package com.andabazaar.repository;

import com.andabazaar.repository.entity.DailyEggRate;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyEggRateRepository extends JpaRepository<DailyEggRate, Long> {
    Optional<DailyEggRate> findByCityIdAndRateDate(Long cityId, LocalDate rateDate);

    boolean existsByCityIdAndRateDate(Long cityId, LocalDate rateDate);

    Optional<DailyEggRate> findTopByCityIdAndRateDateBeforeOrderByRateDateDesc(Long cityId, LocalDate rateDate);
}
