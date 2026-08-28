package com.andabazaar.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.andabazaar.enums.SubscriptionStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "user_subscriptions",
    indexes = {
        @Index(
            name = "idx_user_subscription_user",
            columnList = "user_id"
        ),
        @Index(
            name = "idx_user_subscription_expiry",
            columnList = "end_date"
        )
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
        name = "user_id",
        nullable = false
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "plan_id",
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
    private SubscriptionStatus status =
            SubscriptionStatus.PENDING;

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