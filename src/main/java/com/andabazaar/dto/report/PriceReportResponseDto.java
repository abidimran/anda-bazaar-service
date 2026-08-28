package com.andabazaar.dto.report;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class PriceReportResponseDto {

    private Long id;

    private Long userId;
    private String userName;

    private Long marketId;
    private String marketName;
    private String cityName;

    private BigDecimal reportedPrice;

    private String reason;

    private String description;

    private String status;

    private Boolean reviewed;

    private String adminRemarks;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}