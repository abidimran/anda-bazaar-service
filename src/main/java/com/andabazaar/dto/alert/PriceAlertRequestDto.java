package com.andabazaar.dto.alert;

import java.math.BigDecimal;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceAlertRequestDto {

    private Long userId;

    private Long marketId;

    private BigDecimal targetPrice;

    private String condition;

    private Boolean active;
}