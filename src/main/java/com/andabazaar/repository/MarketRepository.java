package com.andabazaar.repository;

import com.andabazaar.repository.entity.Market;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketRepository extends JpaRepository<Market, Long> {
    // Dashboard
    long countByActiveTrue();
}
