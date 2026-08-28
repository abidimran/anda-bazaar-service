package com.andabazaar.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "egg_prices",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"market_id", "price_date"}
        )
    },
    indexes = {
        @Index(
            name = "idx_egg_price_market_date",
            columnList = "market_id, price_date")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EggPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "market_id",
        nullable = false
    )
    private Market market;

    @Column(
        name = "price_date",
        nullable = false
    )
    private LocalDate priceDate;

    @Column(
        nullable = false,
        precision = 10,
        scale = 2
    )
    private BigDecimal pricePerEgg;

    @Column(
        nullable = false,
        precision = 10,
        scale = 2
    )
    private BigDecimal pricePerTray;

    @Column(
        precision = 10,
        scale = 2
    )
    private BigDecimal previousPrice;

    @Column(length = 20)
    private String priceChangeType;

    @Column(
        precision = 10,
        scale = 2
    )
    private BigDecimal priceChangeAmount;

    @Column(length = 500)
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}