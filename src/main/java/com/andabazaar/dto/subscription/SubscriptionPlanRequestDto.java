package com.andabazaar.dto.subscription;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlanRequestDto {

    @NotBlank(message = "Plan name is required")
    @Size(max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 day")
    private Integer durationDays;

    @NotNull(message = "Price is required")
    @DecimalMin(
        value = "0.00",
        message = "Price cannot be negative"
    )
    private BigDecimal price;
}