
package com.andabazaar.dto.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.andabazaar.enums.PaymentStatus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDto {

    private Long id;

    private Long userId;

    private Long planId;

    private String planName;

    private BigDecimal amount;

    private String currency;

    private String transactionId;

    private String orderId;

    private String razorpayOrderId;

    private String razorpayPaymentId;

    private String razorpaySignature;

    private String productId;

    private PaymentStatus status;

    private String failureReason;

    private LocalDateTime paidAt;

    private LocalDateTime createdAt;
}

