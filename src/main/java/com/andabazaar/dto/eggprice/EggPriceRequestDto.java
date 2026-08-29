package com.andabazaar.dto.eggprice;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EggPriceRequestDto {
    @NotNull(message = "Market ID is required")
    private Long marketId;

    @NotNull(message = "Price date is required")
    private LocalDate priceDate;

    @NotNull(message = "Price per egg is required")
    @DecimalMin(
        value = "0.01",
        message = "Price must be greater than 0")
    private BigDecimal pricePerEgg;

    @NotNull(message = "Price per tray is required")
    @DecimalMin(
        value = "0.01",
        message = "Price must be greater than 0")
    private BigDecimal pricePerTray;

    @Size(max = 500)
    private String remarks;
}
