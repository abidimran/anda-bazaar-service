package com.andabazaar.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "price_history",
    indexes = {
        @Index(
            name = "idx_price_history_market",
            columnList = "market_id"
        ),
        @Index(
            name = "idx_price_history_date",
            columnList = "price_date"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceHistory {

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

    @Column(precision = 10, scale = 2)
    private BigDecimal priceChange;

    @Column(precision = 10, scale = 2)
    private BigDecimal priceChangePercentage;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}