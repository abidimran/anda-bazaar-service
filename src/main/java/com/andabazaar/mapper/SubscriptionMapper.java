package com.andabazaar.mapper;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.andabazaar.dto.subscription.SubscriptionResponseDto;
import com.andabazaar.entity.UserSubscription;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "plan.id", target = "planId")
    @Mapping(source = "plan.name", target = "planName")
    @Mapping(target = "daysRemaining", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "durationDays", ignore = true)
    SubscriptionResponseDto toDto(UserSubscription subscription);

    @AfterMapping
    default void computeFields(UserSubscription subscription, @MappingTarget SubscriptionResponseDto dto) {
        LocalDate today = LocalDate.now();

        if (subscription.getEndDate() != null && !subscription.getEndDate().isBefore(today)) {
            dto.setDaysRemaining(ChronoUnit.DAYS.between(today, subscription.getEndDate()));
            dto.setActive(true);
        } else {
            dto.setDaysRemaining(0L);
            dto.setActive(false);
        }

        if (subscription.getStartDate() != null && subscription.getEndDate() != null) {
            dto.setDurationDays((int) ChronoUnit.DAYS.between(subscription.getStartDate(), subscription.getEndDate()));
        }
    }
}
