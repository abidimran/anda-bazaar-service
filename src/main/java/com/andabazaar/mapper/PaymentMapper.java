package com.andabazaar.mapper;

import org.springframework.stereotype.Component;

import com.andabazaar.dto.payment.PaymentResponseDto;
import com.andabazaar.entity.Payment;

@Component
public class PaymentMapper {

    public PaymentResponseDto toDto(Payment payment) {

        if (payment == null) {
            return null;
        }

        return PaymentResponseDto.builder()
                .id(payment.getId())

                .userId(
                        payment.getUser() != null
                                ? payment.getUser().getId()
                                : null
                )

                .planId(
                        payment.getSubscriptionPlan() != null
                                ? payment.getSubscriptionPlan().getId()
                                : null
                )

                .planName(
                        payment.getSubscriptionPlan() != null
                                ? payment.getSubscriptionPlan().getName()
                                : null
                )

                .amount(payment.getAmount())
                .currency(payment.getCurrency())

                .transactionId(
                        payment.getTransactionId()
                )

                .orderId(
                        payment.getOrderId()
                )

                .razorpayOrderId(
                        payment.getRazorpayOrderId()
                )

                .razorpayPaymentId(
                        payment.getRazorpayPaymentId()
                )

                .razorpaySignature(
                        payment.getRazorpaySignature()
                )

                .productId(
                        payment.getProductId()
                )

                .status(
                        payment.getStatus()
                )

                .failureReason(
                        payment.getFailureReason()
                )

                .paidAt(
                        payment.getPaidAt()
                )

                .createdAt(
                        payment.getCreatedAt()
                )

                .build();
    }
}