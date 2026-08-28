package com.andabazaar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andabazaar.entity.PriceAlert;

public interface PriceAlertRepository
        extends JpaRepository<PriceAlert, Long> {

    List<PriceAlert> findByUserIdOrderByCreatedAtDesc( Long userId);

    List<PriceAlert> findByUserIdAndActiveTrueOrderByCreatedAtDesc( Long userId);

    long countByUserId( Long userId);

    long countByUserIdAndActiveTrue( Long userId);
}