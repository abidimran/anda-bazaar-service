
package com.andabazaar.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.andabazaar.entity.Payment;
import com.andabazaar.enums.PaymentStatus;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionId(
            String transactionId
    );

    Optional<Payment> findByOrderId(
            String orderId
    );

    Optional<Payment> findByRazorpayOrderId(
            String razorpayOrderId
    );

    Optional<Payment> findByRazorpayPaymentId(
            String razorpayPaymentId
    );

    List<Payment> findByUserIdOrderByCreatedAtDesc(
            Long userId
    );

    List<Payment> findByUserIdAndStatusOrderByCreatedAtDesc(
            Long userId,
            PaymentStatus status
    );

    boolean existsByTransactionId(
            String transactionId
    );

    boolean existsByRazorpayOrderId(
            String razorpayOrderId
    );

    boolean existsByRazorpayPaymentId(
            String razorpayPaymentId
    );

    @Query("""
        SELECT COALESCE(SUM(p.amount), 0)
        FROM Payment p
        WHERE p.status = com.andabazaar.enums.PaymentStatus.SUCCESS
    """)
    BigDecimal calculateTotalRevenue();
}

