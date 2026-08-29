package com.andabazaar.dto.eggprice;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EggPriceResponseDto {
    private Long id;

    private Long marketId;

    private String marketName;

    private Long cityId;

    private String cityName;

    private LocalDate priceDate;

    private BigDecimal pricePerEgg;

    private BigDecimal pricePerTray;

    private BigDecimal previousPrice;

    private String priceChangeType;

    private BigDecimal priceChangeAmount;

    private String remarks;

    private Boolean active;
}
