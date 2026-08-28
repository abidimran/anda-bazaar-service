package com.andabazaar.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "price_reports",
    indexes = {
        @Index(
            name = "idx_price_report_user",
            columnList = "user_id"),
        @Index(
            name = "idx_price_report_market",
            columnList = "market_id"),
        @Index(
            name = "idx_price_report_status",
            columnList = "status"),
        @Index(
            name = "idx_price_report_created",
            columnList = "created_at")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "user_id",
        nullable = false
    )
    private User user;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "market_id",
        nullable = false
    )
    private Market market;

    @Column(
        nullable = false,
        precision = 10,
        scale = 2
    )
    private BigDecimal reportedPrice;

    @Column(
        nullable = false,
        length = 500
    )
    private String reason;

    @Column(
        length = 1000
    )
    private String description;

    @Column(
        nullable = false,
        length = 30
    )
    @Builder.Default
    private String status = "PENDING";

    @Column(
        nullable = false
    )
    @Builder.Default
    private Boolean reviewed = false;

    @Column(
        length = 1000
    )
    private String adminRemarks;

    @Column(
        nullable = false,
        updatable = false
    )
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}