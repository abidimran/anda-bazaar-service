package com.andabazaar.serviceimpl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.dto.subscription.SubscribeRequestDto;
import com.andabazaar.dto.subscription.SubscriptionPlanRequestDto;
import com.andabazaar.dto.subscription.SubscriptionPlanResponseDto;
import com.andabazaar.dto.subscription.SubscriptionResponseDto;
import com.andabazaar.entity.SubscriptionPlan;
import com.andabazaar.entity.User;
import com.andabazaar.entity.UserSubscription;
import com.andabazaar.enums.SubscriptionStatus;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.SubscriptionPlanRepository;
import com.andabazaar.repository.UserRepository;
import com.andabazaar.repository.UserSubscriptionRepository;
import com.andabazaar.service.SubscriptionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl
        implements SubscriptionService {

    private final SubscriptionPlanRepository planRepository;

    private final UserSubscriptionRepository
            subscriptionRepository;

    private final UserRepository userRepository;

    @Override
    public SubscriptionPlanResponseDto createPlan( SubscriptionPlanRequestDto request) {

        if (planRepository.existsByNameIgnoreCase(
                request.getName())) {

            throw new BadRequestException(
                    "Subscription plan already exists");
        }

        SubscriptionPlan plan =
                SubscriptionPlan.builder()
                        .name(request.getName())
                        .description(request.getDescription())
                        .durationDays(request.getDurationDays())
                        .price(request.getPrice())
                        .active(true)
                        .build();

        return mapPlan(
                planRepository.save(plan));
    }

    @Override
    public SubscriptionPlanResponseDto updatePlan( Long id, SubscriptionPlanRequestDto request) {

        SubscriptionPlan plan =
                findPlan(id);

        plan.setName(request.getName());
        plan.setDescription(request.getDescription());
        plan.setDurationDays(request.getDurationDays());
        plan.setPrice(request.getPrice());

        return mapPlan(
                planRepository.save(plan));
    }

    @Override
    @Transactional(readOnly = true)
    public SubscriptionPlanResponseDto getPlanById( Long id) {

        return mapPlan(findPlan(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionPlanResponseDto>
            getActivePlans() {

        return planRepository
                .findByActiveTrueOrderByPriceAsc()
                .stream()
                .map(this::mapPlan)
                .toList();
    }

    @Override
    public void deactivatePlan(Long id) {

        SubscriptionPlan plan = findPlan(id);

        plan.setActive(false);

        planRepository.save(plan);
    }

    @Override
    public SubscriptionResponseDto subscribe( Long userId, SubscribeRequestDto request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: "
                                        + userId));

        SubscriptionPlan plan =
                findPlan(request.getPlanId());

        if (!Boolean.TRUE.equals(plan.getActive())) {

            throw new BadRequestException(
                    "Subscription plan is not active");
        }

        LocalDate today = LocalDate.now();

        LocalDate startDate = today;

        LocalDate endDate =
                startDate.plusDays( plan.getDurationDays());

        UserSubscription subscription =
                UserSubscription.builder()
                        .user(user)
                        .plan(plan)
                        .startDate(startDate)
                        .endDate(endDate)
                        .status( SubscriptionStatus.ACTIVE )
                        .activatedAt( java.time.LocalDateTime.now()
                        )
                        .build();

        return mapSubscription(
                subscriptionRepository.save(subscription));
    }

    @Override
    @Transactional(readOnly = true)
    public SubscriptionResponseDto
            getCurrentSubscription(Long userId) {

        UserSubscription subscription =
                subscriptionRepository
                    .findFirstByUserIdAndStatusOrderByEndDateDesc( userId, SubscriptionStatus.ACTIVE )
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                "No active subscription found"
                            ));

        if (subscription.getEndDate()
                .isBefore(LocalDate.now())) {

            throw new BadRequestException(
                    "Subscription has expired");
        }

        return mapSubscription(subscription);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionResponseDto>
            getSubscriptionHistory(Long userId) {

        return subscriptionRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapSubscription)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasActiveSubscription( Long userId) {

        return subscriptionRepository
                .findFirstByUserIdAndStatusOrderByEndDateDesc( userId, SubscriptionStatus.ACTIVE )
                .map(subscription ->
                        !subscription.getEndDate()
                                .isBefore(LocalDate.now()))
                .orElse(false);
    }

    @Override
    public void expireSubscriptions() {

        LocalDate today = LocalDate.now();

        List<UserSubscription> subscriptions =
                subscriptionRepository
                    .findByStatusAndEndDateLessThan( SubscriptionStatus.ACTIVE, today);

        for (UserSubscription subscription : subscriptions) {

            subscription.setStatus( SubscriptionStatus.EXPIRED);
        }

        subscriptionRepository.saveAll( subscriptions);
    }

    private SubscriptionPlan findPlan(Long id) {

        return planRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subscription plan not found with id: "
                                        + id));
    }

    private SubscriptionResponseDto
            mapSubscription( UserSubscription subscription) {

        LocalDate today = LocalDate.now();

        long daysRemaining = 0;

        if (!subscription.getEndDate()
                .isBefore(today)) {

            daysRemaining =
                    ChronoUnit.DAYS.between( today, subscription.getEndDate());
        }

        return SubscriptionResponseDto.builder()
                .id(subscription.getId())
                .userId( subscription.getUser().getId()
                )
                .planId( subscription.getPlan().getId()
                )
                .planName( subscription.getPlan().getName()
                )
                .durationDays( subscription.getPlan()
                                .getDurationDays()
                )
                .startDate( subscription.getStartDate()
                )
                .endDate( subscription.getEndDate()
                )
                .status( subscription.getStatus()
                )
                .daysRemaining(daysRemaining)
                .active( subscription.getStatus()
                                == SubscriptionStatus.ACTIVE
                        && !subscription.getEndDate()
                                .isBefore(today)
                )
                .build();
    }

    private SubscriptionPlanResponseDto
            mapPlan(SubscriptionPlan plan) {

        return SubscriptionPlanResponseDto.builder()
                .id(plan.getId())
                .name(plan.getName())
                .description(plan.getDescription())
                .durationDays(plan.getDurationDays())
                .price(plan.getPrice())
                .active(plan.getActive())
                .build();
    }
}