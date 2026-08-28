
package com.andabazaar.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.payment.PaymentRequestDto;
import com.andabazaar.dto.payment.PaymentResponseDto;
import com.andabazaar.dto.payment.PaymentVerificationDto;
import com.andabazaar.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/user/{userId}/create")
    public ResponseEntity<PaymentResponseDto> createPayment(
            @PathVariable Long userId,
            @Valid @RequestBody PaymentRequestDto request) {

        return ResponseEntity.ok(
                paymentService.createPayment(
                        userId,
                        request.getPlanId()
                )
        );
    }

    @PostMapping("/user/{userId}/verify")
    public ResponseEntity<PaymentResponseDto> verifyPayment(
            @PathVariable Long userId,
            @Valid @RequestBody PaymentVerificationDto request) {

        return ResponseEntity.ok(
                paymentService.verifyPayment(
                        userId,
                        request
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> getPayment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                paymentService.getPaymentById(id)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentResponseDto>> getUserPayments(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                paymentService.getUserPayments(userId)
        );
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<PaymentResponseDto> getPaymentByTransaction(
            @PathVariable String transactionId) {

        return ResponseEntity.ok(
                paymentService.getPaymentByTransactionId(
                        transactionId
                )
        );
    }
}

