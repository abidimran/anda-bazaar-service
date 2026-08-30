package com.andabazaar.repository;

import com.andabazaar.repository.entity.Payment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTransactionId(String transactionId);

    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);

    List<Payment> findByUserIdOrderByCreatedAtDesc(Long userId);
}
