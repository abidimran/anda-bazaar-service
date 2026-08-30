
package com.andabazaar.serviceimpl;

import com.andabazaar.dto.payment.PaymentResponseDto;
import com.andabazaar.dto.payment.PaymentVerificationDto;
import com.andabazaar.enums.PaymentStatus;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.mapper.PaymentMapper;
import com.andabazaar.payment.RazorpayService;
import com.andabazaar.repository.PaymentRepository;
import com.andabazaar.repository.UserRepository;
import com.andabazaar.repository.entity.Payment;
import com.andabazaar.repository.entity.User;
import com.andabazaar.service.PaymentService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.razorpay.Order;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    private final UserRepository userRepository;

    private final RazorpayService razorpayService;

    private final PaymentMapper paymentMapper;

    @Override
    public PaymentResponseDto createPayment(Long userId, BigDecimal amount) {
        User user = findUser(userId);
        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Amount must be greater than zero");
        }

        String receipt =
                "AB_" + userId + "_" + System.currentTimeMillis();
        try {
            Order order = razorpayService.createOrder( amount, "INR", receipt);
            String razorpayOrderId = order.get("id");
            if (razorpayOrderId == null ||
                    razorpayOrderId.isBlank()) {
                throw new BadRequestException("Razorpay order ID was not generated");
            }
            Payment payment =
                    Payment.builder()
                            .user(user)
                            .amount(amount)
                            .currency("INR")
                            .razorpayOrderId( razorpayOrderId )
                            .orderId( razorpayOrderId )
                            .status(PaymentStatus.PENDING )
                            .build();
            Payment savedPayment = paymentRepository.save(payment);
            return paymentMapper.toDto( savedPayment);
        } catch (RazorpayException e) {
            throw new BadRequestException("Unable to create Razorpay order");
        }
    }

    @Override
    public PaymentResponseDto verifyPayment(Long userId, PaymentVerificationDto request) {
        User user = findUser(userId);
        if (request == null) {
            throw new BadRequestException("Payment verification data is required");
        }

        if (request.getRazorpayOrderId() == null ||
                request.getRazorpayOrderId().isBlank()) {
            throw new BadRequestException("Razorpay order ID is required");
        }

        if (request.getRazorpayPaymentId() == null ||
                request.getRazorpayPaymentId().isBlank()) {
            throw new BadRequestException("Razorpay payment ID is required");
        }

        if (request.getRazorpaySignature() == null ||
                request.getRazorpaySignature().isBlank()) {
            throw new BadRequestException("Razorpay signature is required");
        }

        Payment payment =
                paymentRepository
                        .findByRazorpayOrderId( request.getRazorpayOrderId() )
                        .orElseThrow(() -> new ResourceNotFoundException("Payment order not found"));
        if (payment.getUser() == null ||
                payment.getUser().getId() == null ||
                !payment.getUser()
                        .getId()
                        .equals(user.getId())) {
            throw new BadRequestException("Payment does not belong to this user");
        }

        if (payment.getStatus() ==
                PaymentStatus.SUCCESS) {
            return paymentMapper.toDto(payment);
        }

        // -----------------------------------------------------
        // VERIFY RAZORPAY SIGNATURE
        // -----------------------------------------------------
        try {
            boolean verified =
                    razorpayService.verifySignature(
                            payment.getRazorpayOrderId(),
                            request.getRazorpayPaymentId(),
                            request.getRazorpaySignature());
            if (!verified) {
                payment.setStatus(PaymentStatus.FAILED);
                payment.setFailureReason("Invalid Razorpay payment signature");
                paymentRepository.save(payment);
                throw new BadRequestException("Payment verification failed");
            }
        } catch (RazorpayException e) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason("Razorpay signature verification error");
            paymentRepository.save(payment);
            throw new BadRequestException("Payment verification failed");
        }

        // -----------------------------------------------------
        // UPDATE PAYMENT
        // -----------------------------------------------------
        payment.setRazorpayPaymentId( request.getRazorpayPaymentId());
        payment.setRazorpaySignature( request.getRazorpaySignature());
        payment.setTransactionId( request.getRazorpayPaymentId());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());
        payment.setFailureReason(null);
        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toDto( savedPayment);
    }

    @Override
    public void processRazorpayWebhook(String payload) {
        if (payload == null ||
                payload.isBlank()) {
            return;
        }

        try {
            JSONObject json = new JSONObject(payload);
            String event = json.optString("event");
            JSONObject payloadObject = json.optJSONObject("payload");
            if (payloadObject == null) {
                return;
            }
            JSONObject paymentObject = payloadObject.optJSONObject("payment");
            if (paymentObject == null) {
                return;
            }
            JSONObject entity = paymentObject.optJSONObject("entity");
            if (entity == null) {
                return;
            }
            String razorpayPaymentId = entity.optString("id", null);
            String razorpayOrderId = entity.optString("order_id", null);
            if (razorpayOrderId == null ||
                    razorpayOrderId.isBlank()) {
                return;
            }
            Payment payment =
                    paymentRepository
                            .findByRazorpayOrderId( razorpayOrderId )
                            .orElse(null);
            if (payment == null) {
                return;
            }
            if ("payment.captured".equals(event) ||
                    "order.paid".equals(event)) {
                if (payment.getStatus() ==
                        PaymentStatus.SUCCESS) {
                    return;
                }
                if (razorpayPaymentId != null &&
                        !razorpayPaymentId.isBlank()) {
                    payment.setRazorpayPaymentId( razorpayPaymentId);
                    payment.setTransactionId( razorpayPaymentId);
                }
                payment.setStatus(PaymentStatus.SUCCESS);
                payment.setPaidAt(LocalDateTime.now());
                payment.setFailureReason(null);
                paymentRepository.save(payment);
                return;
            }
            if ("payment.failed".equals(event)) {
                if (payment.getStatus() ==
                        PaymentStatus.SUCCESS) {
                    return;
                }
                String errorDescription = entity.optString("error_description", "Payment failed");
                if (razorpayPaymentId != null &&
                        !razorpayPaymentId.isBlank()) {
                    payment.setRazorpayPaymentId( razorpayPaymentId);
                }
                payment.setStatus(PaymentStatus.FAILED);
                payment.setFailureReason( errorDescription);
                paymentRepository.save(payment);
            }
        } catch (Exception e) {
            throw new BadRequestException("Unable to process Razorpay webhook");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentById(Long id) {
        Payment payment =
                paymentRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        return paymentMapper.toDto(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getUserPayments(Long userId) {
        findUser(userId);
        return paymentRepository
                .findByUserIdOrderByCreatedAtDesc( userId )
                .stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentByTransactionId(String transactionId) {
        if (transactionId == null ||
                transactionId.isBlank()) {
            throw new BadRequestException("Transaction ID is required");
        }

        Payment payment =
                paymentRepository
                        .findByTransactionId( transactionId )
                        .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        return paymentMapper.toDto(payment);
    }

    private User findUser(Long id) {
        if (id == null) {
            throw new BadRequestException("User ID is required");
        }

        return userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
