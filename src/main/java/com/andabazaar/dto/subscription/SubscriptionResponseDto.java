package com.andabazaar.dto.subscription;

import java.time.LocalDate;

import com.andabazaar.enums.SubscriptionStatus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionResponseDto {

    private Long id;

    private Long userId;

    private Long planId;

    private String planName;

    private Integer durationDays;

    private LocalDate startDate;

    private LocalDate endDate;

    private SubscriptionStatus status;

    private Long daysRemaining;

    private Boolean active;
}