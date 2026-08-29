
package com.andabazaar.repository.entity;

import com.andabazaar.enums.PaymentStatus;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.*;

@Entity
@Table(
    name = "payments",
    indexes = {
        @Index(
            name = "idx_payment_user",
            columnList = "userId"),
        @Index(
            name = "idx_payment_transaction",
            columnList = "transactionId"),
        @Index(
            name = "idx_payment_razorpay_order",
            columnList = "razorpayOrderId")
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
        name = "userId",
        nullable = false
    )
    private User user;

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
        name = "transactionId",
        unique = true,
        length = 200
    )
    private String transactionId;

    @Column(
        name = "orderId",
        length = 200
    )
    private String orderId;

    @Column(
        name = "razorpayOrderId",
        unique = true,
        length = 200
    )
    private String razorpayOrderId;

    @Column(
        name = "razorpayPaymentId",
        unique = true,
        length = 200
    )
    private String razorpayPaymentId;

    @Column(
        name = "razorpaySignature",
        length = 500
    )
    private String razorpaySignature;

    @Column(
        name = "productId",
        length = 200
    )
    private String productId;

    @Enumerated(EnumType.STRING)
    @Column(
        nullable = false,
        length = 30
    )
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

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
