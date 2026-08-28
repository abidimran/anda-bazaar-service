package com.andabazaar.dto.analytics;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceAnalyticsResponseDto {

    private Long marketId;

    private String marketName;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal lowestPrice;

    private BigDecimal highestPrice;

    private BigDecimal averagePrice;

    private BigDecimal currentPrice;

    private BigDecimal priceChange;

    private BigDecimal priceChangePercentage;
}