package com.andabazaar.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andabazaar.entity.UserSubscription;
import com.andabazaar.enums.SubscriptionStatus;

public interface UserSubscriptionRepository
        extends JpaRepository<UserSubscription, Long> {

    // =========================
    // CURRENT SUBSCRIPTION
    // =========================

    Optional<UserSubscription>
    findFirstByUserIdAndStatusOrderByEndDateDesc( Long userId, SubscriptionStatus status);

    // =========================
    // USER + PLAN SUBSCRIPTION
    // =========================

    Optional<UserSubscription>
    findByUserIdAndPlanId( Long userId, Long planId);

    // =========================
    // SUBSCRIPTION HISTORY
    // =========================

    List<UserSubscription>
    findByUserIdOrderByCreatedAtDesc( Long userId);

    // =========================
    // STATUS + EXACT END DATE
    // =========================

    List<UserSubscription>
    findByStatusAndEndDate( SubscriptionStatus status, LocalDate endDate);

    // =========================
    // EXPIRED SUBSCRIPTIONS
    // =========================

    List<UserSubscription>
    findByStatusAndEndDateLessThan( SubscriptionStatus status, LocalDate date);

    // =========================
    // ACTIVE SUBSCRIPTIONS
    // =========================

    List<UserSubscription>
    findByStatusAndEndDateGreaterThanEqual( SubscriptionStatus status, LocalDate date);

    // =========================
    // DASHBOARD - STATUS COUNT
    // =========================

    long countByStatus( SubscriptionStatus status);
}