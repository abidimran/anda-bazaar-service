package com.andabazaar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andabazaar.repository.entity.SubscriptionPlan;

public interface SubscriptionPlanRepository
        extends JpaRepository<SubscriptionPlan, Long> {

    Optional<SubscriptionPlan> findByNameIgnoreCase( String name);

    boolean existsByNameIgnoreCase( String name);

    List<SubscriptionPlan> findByActiveTrueOrderByPriceAsc();
}