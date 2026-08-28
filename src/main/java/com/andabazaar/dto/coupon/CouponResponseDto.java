package com.andabazaar.dto.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.andabazaar.enums.CouponStatus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponResponseDto {

    private Long id;

    private String code;

    private String description;

    private BigDecimal discountAmount;

    private Boolean percentage;

    private BigDecimal minimumOrderAmount;

    private Integer usageLimit;

    private Integer usedCount;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private CouponStatus status;

    private Boolean active;

    private Boolean expired;

    private Boolean usable;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}