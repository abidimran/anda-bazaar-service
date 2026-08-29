package com.andabazaar.repository.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.andabazaar.enums.SubscriptionStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "userSubscriptions",
    indexes = {
        @Index(
            name = "idx_user_subscription_user",
            columnList = "userId"),
        @Index(
            name = "idx_user_subscription_expiry",
            columnList = "endDate")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "userId",
        nullable = false
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "planId",
        nullable = false
    )
    private SubscriptionPlan plan;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private SubscriptionStatus status = SubscriptionStatus.PENDING;

    @Column
    private LocalDateTime activatedAt;

    @Column
    private LocalDateTime cancelledAt;

    @Column(length = 500)
    private String remarks;

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