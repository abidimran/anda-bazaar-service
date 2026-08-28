package com.andabazaar.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "price_alerts",
    indexes = {
        @Index(
            name = "idx_price_alert_user",
            columnList = "user_id"
        ),
        @Index(
            name = "idx_price_alert_market",
            columnList = "market_id"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================================================
    // USER
    // =========================================================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false
    )
    private User user;

    // =========================================================
    // MARKET
    // =========================================================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "market_id",
        nullable = false
    )
    private Market market;

    // =========================================================
    // TARGET PRICE
    // =========================================================

    @Column(
        nullable = false,
        precision = 10,
        scale = 2
    )
    private BigDecimal targetPrice;

    // =========================================================
    // ALERT CONDITION
    // DATABASE COLUMN = alert_condition
    // JAVA FIELD = condition
    // =========================================================

    @Column(
        name = "alert_condition",
        nullable = false,
        length = 20
    )
    private String condition;

    // =========================================================
    // ACTIVE
    // =========================================================

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    // =========================================================
    // CREATED AT
    // =========================================================

    @Column(
        nullable = false,
        updatable = false
    )
    private LocalDateTime createdAt;

    // =========================================================
    // UPDATED AT
    // =========================================================

    private LocalDateTime updatedAt;

    // =========================================================
    // PRE PERSIST
    // =========================================================

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    // =========================================================
    // PRE UPDATE
    // =========================================================

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}