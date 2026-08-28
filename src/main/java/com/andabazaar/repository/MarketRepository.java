package com.andabazaar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andabazaar.entity.Market;

public interface MarketRepository
        extends JpaRepository<Market, Long> {

    List<Market> findByCityIdAndActiveTrueOrderByNameAsc( Long cityId);

    List<Market> findByActiveTrueOrderByNameAsc();

    boolean existsByNameIgnoreCaseAndCityId( String name, Long cityId);
    
    // Dashboard
    long countByActiveTrue();
}