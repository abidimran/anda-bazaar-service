package com.andabazaar.dto.comparison;

import java.math.BigDecimal;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketComparisonResponseDto {

    private Long marketId;

    private String marketName;

    private String cityName;

    private BigDecimal currentPrice;

    private BigDecimal lowestPrice;

    private BigDecimal highestPrice;

    private BigDecimal averagePrice;
}