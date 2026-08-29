package com.andabazaar.repository.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dailyEggRates", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"cityId", "rateDate"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyEggRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cityId", nullable = false)
    private City city;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stateId", nullable = false)
    private State state;

    @Column(name = "rateDate", nullable = false)
    private LocalDate rateDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal rate;

    @Column(precision = 10, scale = 2)
    private BigDecimal previousRate;

    @Column(length = 20)
    private String trend;

    @Column(precision = 10, scale = 2)
    private BigDecimal change;

    @Column(length = 50)
    private String source;

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
