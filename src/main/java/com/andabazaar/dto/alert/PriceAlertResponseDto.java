package com.andabazaar.dto.alert;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceAlertResponseDto {

    private Long id;

    private Long userId;

    private Long marketId;

    private String marketName;

    private BigDecimal targetPrice;

    private String condition;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}