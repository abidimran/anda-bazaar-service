package com.andabazaar.dto.report;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceReportRequestDto {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Market ID is required")
    private Long marketId;

    @NotNull(message = "Egg price is required")
    @DecimalMin(
        value = "0.01",
        message = "Reported price must be greater than 0"
    )
    private BigDecimal reportedPrice;

    @NotBlank(message = "Report reason is required")
    @Size(
        max = 500,
        message = "Reason cannot exceed 500 characters"
    )
    private String reason;

    @Size(
        max = 1000,
        message = "Description cannot exceed 1000 characters"
    )
    private String description;
}