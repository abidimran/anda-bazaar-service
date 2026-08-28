package com.andabazaar.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.andabazaar.enums.CouponStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "coupons",
    indexes = {
        @Index(
            name = "idx_coupon_code",
            columnList = "code"),
        @Index(
            name = "idx_coupon_status",
            columnList = "status"),
        @Index(
            name = "idx_coupon_expiry",
            columnList = "end_date")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        nullable = false,
        unique = true,
        length = 50
    )
    private String code;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private BigDecimal discountAmount;

    @Column(nullable = false)
    private Boolean percentage;

    @Column(nullable = false)
    private BigDecimal minimumOrderAmount;

    @Column(nullable = false)
    private Integer usageLimit;

    @Builder.Default
    @Column(nullable = false)
    private Integer usedCount = 0;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(
        nullable = false,
        length = 30
    )
    @Builder.Default
    private CouponStatus status = CouponStatus.ACTIVE;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (usedCount == null) {
            usedCount = 0;
        }

        if (active == null) {
            active = true;
        }

        if (status == null) {
            status = CouponStatus.ACTIVE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}