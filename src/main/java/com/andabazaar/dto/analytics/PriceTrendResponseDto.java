package com.andabazaar.dto.analytics;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceTrendResponseDto {

    private LocalDate priceDate;

    private BigDecimal price;

    private BigDecimal change;

    private BigDecimal changePercentage;
}