package com.andabazaar.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.common.PagedResponse;
import com.andabazaar.dto.payment.PaymentRequestDto;
import com.andabazaar.dto.payment.PaymentResponseDto;
import com.andabazaar.dto.payment.PaymentVerificationDto;
import com.andabazaar.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Payments", description = "Payment processing and verification")
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Create Payment")
    @PostMapping("/user/{userId}")
    public ResponseEntity<PaymentResponseDto> createPayment(@PathVariable Long userId,
            @Valid @RequestBody PaymentRequestDto request) {

 return ResponseEntity.ok(paymentService.createPayment(userId, request.getPlanId()));
    }

    @Operation(summary = "Verify Payment")
    @PostMapping("/user/{userId}/verification")
    public ResponseEntity<PaymentResponseDto> verifyPayment(@PathVariable Long userId,
            @Valid @RequestBody PaymentVerificationDto request) {

 return ResponseEntity.ok(paymentService.verifyPayment(userId, request));
    }

    @Operation(summary = "Get Payment")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> getPayment(@PathVariable Long id) {

 return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @Operation(summary = "Get User Payments")
    @GetMapping("/user/{userId}")
    public ResponseEntity<PagedResponse<PaymentResponseDto>>
            getUserPayments(@PathVariable Long userId,
                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(paymentService.getUserPayments(userId), page, size));
    }

    @Operation(summary = "Get Payment By Transaction")
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<PaymentResponseDto> getPaymentByTransaction(@PathVariable String transactionId) {

 return ResponseEntity.ok(paymentService.getPaymentByTransactionId(transactionId));
    }
}
