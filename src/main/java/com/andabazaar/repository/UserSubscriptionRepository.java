package com.andabazaar.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andabazaar.entity.UserSubscription;
import com.andabazaar.enums.SubscriptionStatus;

public interface UserSubscriptionRepository
        extends JpaRepository<UserSubscription, Long> {

    Optional<UserSubscription>
    findFirstByUserIdAndStatusOrderByEndDateDesc( Long userId, SubscriptionStatus status);

    Optional<UserSubscription>
    findByUserIdAndPlanId( Long userId, Long planId);

    List<UserSubscription>
    findByUserIdOrderByCreatedAtDesc( Long userId);

    List<UserSubscription>
    findByStatusAndEndDate( SubscriptionStatus status, LocalDate endDate);

    List<UserSubscription>
    findByStatusAndEndDateLessThan( SubscriptionStatus status, LocalDate date);

    List<UserSubscription>
    findByStatusAndEndDateGreaterThanEqual( SubscriptionStatus status, LocalDate date);

    long countByStatus( SubscriptionStatus status);
}