package com.andabazaar.dto.expectedprice;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpectedPriceRequestDto {

    @NotNull(message = "Market ID is required")
    private Long marketId;

    @NotNull(message = "Expected price is required")
    @DecimalMin(
        value = "0.01",
        message = "Expected price must be greater than 0"
    )
    private BigDecimal expectedPrice;

    @NotNull(message = "Expected date is required")
    private LocalDate expectedDate;

    @Size(
        max = 500,
        message = "Reason cannot exceed 500 characters"
    )
    private String reason;
}