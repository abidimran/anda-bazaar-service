
package com.andabazaar.service;

import com.andabazaar.dto.payment.PaymentResponseDto;
import com.andabazaar.dto.payment.PaymentVerificationDto;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    PaymentResponseDto createPayment( Long userId, BigDecimal amount);
    PaymentResponseDto verifyPayment( Long userId, PaymentVerificationDto request);
    PaymentResponseDto getPaymentById( Long id);
    List<PaymentResponseDto> getUserPayments( Long userId);
    PaymentResponseDto getPaymentByTransactionId( String transactionId);
    void processRazorpayWebhook( String payload);
}
