package com.andabazaar.service;

import java.util.List;

import com.andabazaar.dto.subscription.SubscribeRequestDto;
import com.andabazaar.dto.subscription.SubscriptionPlanRequestDto;
import com.andabazaar.dto.subscription.SubscriptionPlanResponseDto;
import com.andabazaar.dto.subscription.SubscriptionResponseDto;

public interface SubscriptionService {

    SubscriptionPlanResponseDto createPlan( SubscriptionPlanRequestDto request);

    SubscriptionPlanResponseDto updatePlan( Long id, SubscriptionPlanRequestDto request);

    SubscriptionPlanResponseDto getPlanById( Long id);

    List<SubscriptionPlanResponseDto>
    getActivePlans();

    void deactivatePlan(Long id);

    SubscriptionResponseDto subscribe( Long userId, SubscribeRequestDto request);

    SubscriptionResponseDto getCurrentSubscription( Long userId);

    List<SubscriptionResponseDto>
    getSubscriptionHistory(Long userId);

    boolean hasActiveSubscription( Long userId);

    void expireSubscriptions();
}