package com.andabazaar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andabazaar.entity.PriceReport;

public interface PriceReportRepository
        extends JpaRepository<PriceReport, Long> {

    List<PriceReport>
    findAllByOrderByCreatedAtDesc();

    List<PriceReport>
    findByUserIdOrderByCreatedAtDesc( Long userId);

    List<PriceReport>
    findByMarketIdOrderByCreatedAtDesc( Long marketId);

    List<PriceReport>
    findByStatusOrderByCreatedAtDesc( String status);

    List<PriceReport>
    findByReviewedOrderByCreatedAtDesc( Boolean reviewed);

    long countByStatus( String status);

    long countByMarketId( Long marketId);

    long countByUserId( Long userId);
}