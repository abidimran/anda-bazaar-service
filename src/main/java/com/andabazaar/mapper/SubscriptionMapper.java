package com.andabazaar.mapper;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

import com.andabazaar.dto.subscription.SubscriptionResponseDto;
import com.andabazaar.entity.UserSubscription;

@Component
public class SubscriptionMapper {

    public SubscriptionResponseDto toDto(
            UserSubscription subscription) {

        if (subscription == null) {
            return null;
        }

        LocalDate today = LocalDate.now();

        Long daysRemaining = 0L;
        Boolean active = false;

        if (subscription.getEndDate() != null
                && !subscription.getEndDate()
                        .isBefore(today)) {

            daysRemaining = ChronoUnit.DAYS.between(
                    today,
                    subscription.getEndDate()
            );

            active = true;
        }

        Integer durationDays = null;

        if (subscription.getStartDate() != null
                && subscription.getEndDate() != null) {

            durationDays =
                    (int) ChronoUnit.DAYS.between(
                            subscription.getStartDate(),
                            subscription.getEndDate()
                    );
        }

        return SubscriptionResponseDto.builder()
                .id(subscription.getId())
                .userId(
                        subscription.getUser() != null
                                ? subscription.getUser().getId()
                                : null
                )
                .planId(
                        subscription.getPlan() != null
                                ? subscription.getPlan().getId()
                                : null
                )
                .planName(
                        subscription.getPlan() != null
                                ? subscription.getPlan().getName()
                                : null
                )
                .durationDays(durationDays)
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .status(subscription.getStatus())
                .daysRemaining(daysRemaining)
                .active(active)
                .build();
    }
}