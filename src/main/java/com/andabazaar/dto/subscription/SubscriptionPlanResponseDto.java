package com.andabazaar.dto.subscription;

import java.math.BigDecimal;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlanResponseDto {

    private Long id;

    private String name;

    private String description;

    private Integer durationDays;

    private BigDecimal price;

    private Boolean active;
}