package com.andabazaar.repository.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "expectedPrices",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"marketId", "expectedDate"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpectedPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "marketId",
        nullable = false
    )
    private Market market;

    @Column(
        name = "expectedDate",
        nullable = false
    )
    private LocalDate expectedDate;

    @Column(
        nullable = false,
        precision = 10,
        scale = 2
    )
    private BigDecimal expectedPrice;

    @Column(length = 500)
    private String reason;

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