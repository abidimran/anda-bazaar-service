package com.andabazaar.dto.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponRequestDto {

    @NotBlank(message = "Coupon code is required")
    @Size(max = 50, message = "Coupon code cannot exceed 50 characters")
    private String code;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    @NotNull(message = "Discount amount is required")
    @DecimalMin(
        value = "0.01",
        message = "Discount amount must be greater than 0")
    private BigDecimal discountAmount;

    @NotNull(message = "Percentage flag is required")
    private Boolean percentage;

    @NotNull(message = "Minimum order amount is required")
    @DecimalMin(
        value = "0.00",
        message = "Minimum order amount cannot be negative")
    private BigDecimal minimumOrderAmount;

    @NotNull(message = "Usage limit is required")
    @Min(
        value = 1,
        message = "Usage limit must be at least 1")
    private Integer usageLimit;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;
}