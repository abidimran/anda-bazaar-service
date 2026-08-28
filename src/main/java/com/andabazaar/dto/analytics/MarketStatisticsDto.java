package com.andabazaar.dto.analytics;

import java.math.BigDecimal;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketStatisticsDto {

    private Long marketId;

    private String marketName;

    private BigDecimal lowestPrice;

    private BigDecimal highestPrice;

    private BigDecimal averagePrice;

    private BigDecimal currentPrice;

    private Long totalPriceRecords;
}