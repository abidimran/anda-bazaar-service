package com.andabazaar.dto.subscription;

import jakarta.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscribeRequestDto {

    @NotNull(message = "Plan ID is required")
    private Long planId;
}