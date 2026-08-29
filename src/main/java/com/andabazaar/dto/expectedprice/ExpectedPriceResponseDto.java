package com.andabazaar.dto.expectedprice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpectedPriceResponseDto {
    private Long id;

    private Long marketId;

    private String marketName;

    private String cityName;

    private BigDecimal expectedPrice;

    private LocalDate expectedDate;

    private String reason;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
