package com.andabazaar.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "coupon_usage",
    indexes = {
        @Index(
            name = "idx_coupon_usage_coupon",
            columnList = "coupon_id"
        ),
        @Index(
            name = "idx_coupon_usage_user",
            columnList = "user_id"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "coupon_id",
        nullable = false
    )
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false
    )
    private User user;

    @Column(
        nullable = false,
        precision = 10,
        scale = 2
    )
    private BigDecimal discountAmount;

    @Column(
        nullable = false,
        precision = 10,
        scale = 2
    )
    private BigDecimal originalAmount;

    @Column(
        nullable = false,
        precision = 10,
        scale = 2
    )
    private BigDecimal finalAmount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime usedAt;

    @PrePersist
    protected void onCreate() {
        usedAt = LocalDateTime.now();
    }
}