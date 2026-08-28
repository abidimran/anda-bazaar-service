
package com.andabazaar.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.andabazaar.enums.PaymentStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "payments",
    indexes = {
        @Index(
            name = "idx_payment_user",
            columnList = "user_id"),
        @Index(
            name = "idx_payment_transaction",
            columnList = "transaction_id"),
        @Index(
            name = "idx_payment_razorpay_order",
            columnList = "razorpay_order_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

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
        name = "subscription_plan_id",
        nullable = false
    )
    private SubscriptionPlan subscriptionPlan;

    @Column(
        nullable = false,
        precision = 10,
        scale = 2
    )
    private BigDecimal amount;

    @Column(
        nullable = false,
        length = 10
    )
    private String currency;

    @Column(
        name = "transaction_id",
        unique = true,
        length = 200
    )
    private String transactionId;

    @Column(
        name = "order_id",
        length = 200
    )
    private String orderId;

    @Column(
        name = "razorpay_order_id",
        unique = true,
        length = 200
    )
    private String razorpayOrderId;

    @Column(
        name = "razorpay_payment_id",
        unique = true,
        length = 200
    )
    private String razorpayPaymentId;

    @Column(
        name = "razorpay_signature",
        length = 500
    )
    private String razorpaySignature;

    @Column(
        name = "product_id",
        length = 200
    )
    private String productId;

    @Enumerated(EnumType.STRING)
    @Column(
        nullable = false,
        length = 30
    )
    @Builder.Default
    private PaymentStatus status =
            PaymentStatus.PENDING;

    @Column(length = 500)
    private String failureReason;

    @Column
    private LocalDateTime paidAt;

    @Column(
        nullable = false,
        updatable = false
    )
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

