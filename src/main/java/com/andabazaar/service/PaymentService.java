
package com.andabazaar.service;

import java.util.List;

import com.andabazaar.dto.payment.PaymentResponseDto;
import com.andabazaar.dto.payment.PaymentVerificationDto;

public interface PaymentService {

    PaymentResponseDto createPayment(
            Long userId,
            Long planId
    );

    PaymentResponseDto verifyPayment(
            Long userId,
            PaymentVerificationDto request
    );

    PaymentResponseDto getPaymentById(
            Long id
    );

    List<PaymentResponseDto> getUserPayments(
            Long userId
    );

    PaymentResponseDto getPaymentByTransactionId(
            String transactionId
    );

    void processRazorpayWebhook(
            String payload
    );
}

