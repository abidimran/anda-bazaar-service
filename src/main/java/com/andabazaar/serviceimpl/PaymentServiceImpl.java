
package com.andabazaar.serviceimpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.dto.payment.PaymentResponseDto;
import com.andabazaar.dto.payment.PaymentVerificationDto;
import com.andabazaar.entity.Payment;
import com.andabazaar.entity.SubscriptionPlan;
import com.andabazaar.entity.User;
import com.andabazaar.entity.UserSubscription;
import com.andabazaar.enums.PaymentStatus;
import com.andabazaar.enums.SubscriptionStatus;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.mapper.PaymentMapper;
import com.andabazaar.payment.RazorpayService;
import com.andabazaar.repository.PaymentRepository;
import com.andabazaar.repository.SubscriptionPlanRepository;
import com.andabazaar.repository.UserRepository;
import com.andabazaar.repository.UserSubscriptionRepository;
import com.andabazaar.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final UserRepository userRepository;

    private final SubscriptionPlanRepository planRepository;

    private final UserSubscriptionRepository subscriptionRepository;

    private final RazorpayService razorpayService;

    private final PaymentMapper paymentMapper;

    // =========================================================
    // CREATE RAZORPAY PAYMENT
    // =========================================================

    @Override
    public PaymentResponseDto createPayment( Long userId, Long planId) {

        User user = findUser(userId);

        SubscriptionPlan plan = findPlan(planId);

        if (!Boolean.TRUE.equals(plan.getActive())) {
            throw new BadRequestException(
                    "Subscription plan is not active");
        }

        BigDecimal amount = plan.getPrice();

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new BadRequestException(
                    "Invalid subscription plan price");
        }

        String receipt =
                "AB_"
                        + userId
                        + "_"
                        + System.currentTimeMillis();

        try {

            Order order =
                    razorpayService.createOrder( amount, "INR", receipt);

            String razorpayOrderId =
                    order.get("id");

            if (razorpayOrderId == null ||
                    razorpayOrderId.isBlank()) {

                throw new BadRequestException(
                        "Razorpay order ID was not generated");
            }

            Payment payment =
                    Payment.builder()
                            .user(user)
                            .subscriptionPlan(plan)
                            .amount(amount)
                            .currency("INR")
                            .razorpayOrderId( razorpayOrderId )
                            .orderId( razorpayOrderId )
                            .status( PaymentStatus.PENDING )
                            .build();

            Payment savedPayment =
                    paymentRepository.save(payment);

            return paymentMapper.toDto(
                    savedPayment);

        } catch (RazorpayException e) {

            throw new BadRequestException(
                    "Unable to create Razorpay order");
        }
    }

    // =========================================================
    // VERIFY PAYMENT
    // =========================================================

    @Override
    public PaymentResponseDto verifyPayment( Long userId, PaymentVerificationDto request) {

        User user = findUser(userId);

        if (request == null) {
            throw new BadRequestException(
                    "Payment verification data is required");
        }

        if (request.getRazorpayOrderId() == null ||
                request.getRazorpayOrderId().isBlank()) {

            throw new BadRequestException(
                    "Razorpay order ID is required");
        }

        if (request.getRazorpayPaymentId() == null ||
                request.getRazorpayPaymentId().isBlank()) {

            throw new BadRequestException(
                    "Razorpay payment ID is required");
        }

        if (request.getRazorpaySignature() == null ||
                request.getRazorpaySignature().isBlank()) {

            throw new BadRequestException(
                    "Razorpay signature is required");
        }

        Payment payment =
                paymentRepository
                        .findByRazorpayOrderId( request.getRazorpayOrderId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment order not found"
                                ));

        if (payment.getUser() == null ||
                payment.getUser().getId() == null ||
                !payment.getUser()
                        .getId()
                        .equals(user.getId())) {

            throw new BadRequestException(
                    "Payment does not belong to this user");
        }

        if (payment.getStatus() ==
                PaymentStatus.SUCCESS) {

            return paymentMapper.toDto(payment);
        }

        SubscriptionPlan plan =
                payment.getSubscriptionPlan();

        if (plan == null) {
            throw new BadRequestException(
                    "Subscription plan not found for payment");
        }

        if (!Boolean.TRUE.equals(
                plan.getActive())) {

            throw new BadRequestException(
                    "Subscription plan is not active");
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

                payment.setStatus( PaymentStatus.FAILED);

                payment.setFailureReason( "Invalid Razorpay payment signature");

                paymentRepository.save(payment);

                throw new BadRequestException(
                        "Payment verification failed");
            }

        } catch (RazorpayException e) {

            payment.setStatus( PaymentStatus.FAILED);

            payment.setFailureReason( "Razorpay signature verification error");

            paymentRepository.save(payment);

            throw new BadRequestException(
                    "Payment verification failed");
        }

        // -----------------------------------------------------
        // UPDATE PAYMENT
        // -----------------------------------------------------

        payment.setRazorpayPaymentId( request.getRazorpayPaymentId());

        payment.setRazorpaySignature( request.getRazorpaySignature());

        payment.setTransactionId( request.getRazorpayPaymentId());

        payment.setStatus( PaymentStatus.SUCCESS);

        payment.setPaidAt( LocalDateTime.now());

        payment.setFailureReason(null);

        Payment savedPayment =
                paymentRepository.save(payment);

        // -----------------------------------------------------
        // ACTIVATE SUBSCRIPTION
        // -----------------------------------------------------

        activateSubscriptionIfRequired( user, plan);

        return paymentMapper.toDto(
                savedPayment);
    }

    // =========================================================
    // RAZORPAY WEBHOOK
    // =========================================================

    @Override
    public void processRazorpayWebhook( String payload) {

        if (payload == null ||
                payload.isBlank()) {

            return;
        }

        try {

            JSONObject json =
                    new JSONObject(payload);

            String event =
                    json.optString("event");

            JSONObject payloadObject =
                    json.optJSONObject("payload");

            if (payloadObject == null) {
                return;
            }

            JSONObject paymentObject =
                    payloadObject.optJSONObject( "payment");

            if (paymentObject == null) {

                return;
            }

            JSONObject entity =
                    paymentObject.optJSONObject( "entity");

            if (entity == null) {

                return;
            }

            String razorpayPaymentId =
                    entity.optString( "id", null);

            String razorpayOrderId =
                    entity.optString( "order_id", null);

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

            // =================================================
            // PAYMENT CAPTURED
            // =================================================

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

                payment.setStatus( PaymentStatus.SUCCESS);

                payment.setPaidAt( LocalDateTime.now());

                payment.setFailureReason(null);

                paymentRepository.save(payment);

                activateSubscriptionIfRequired( payment.getUser(), payment.getSubscriptionPlan());

                return;
            }

            // =================================================
            // PAYMENT FAILED
            // =================================================

            if ("payment.failed".equals(event)) {

                if (payment.getStatus() ==
                        PaymentStatus.SUCCESS) {

                    return;
                }

                String errorDescription =
                        entity.optString( "error_description", "Payment failed");

                if (razorpayPaymentId != null &&
                        !razorpayPaymentId.isBlank()) {

                    payment.setRazorpayPaymentId( razorpayPaymentId);
                }

                payment.setStatus( PaymentStatus.FAILED);

                payment.setFailureReason( errorDescription);

                paymentRepository.save(payment);
            }

        } catch (Exception e) {

            throw new BadRequestException(
                    "Unable to process Razorpay webhook");
        }
    }

    // =========================================================
    // GET PAYMENT BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentById( Long id) {

        Payment payment =
                paymentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found with id: "
                                                + id
                                ));

        return paymentMapper.toDto(payment);
    }

    // =========================================================
    // GET USER PAYMENTS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getUserPayments( Long userId) {

        findUser(userId);

        return paymentRepository
                .findByUserIdOrderByCreatedAtDesc( userId )
                .stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    // =========================================================
    // GET PAYMENT BY TRANSACTION ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentByTransactionId( String transactionId) {

        if (transactionId == null ||
                transactionId.isBlank()) {

            throw new BadRequestException(
                    "Transaction ID is required");
        }

        Payment payment =
                paymentRepository
                        .findByTransactionId( transactionId )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found"
                                ));

        return paymentMapper.toDto(payment);
    }

    // =========================================================
    // ACTIVATE SUBSCRIPTION
    // =========================================================

    private void activateSubscriptionIfRequired( User user, SubscriptionPlan plan) {

        if (user == null ||
                user.getId() == null) {

            throw new BadRequestException(
                    "Payment user is invalid");
        }

        if (plan == null ||
                plan.getId() == null) {

            throw new BadRequestException(
                    "Subscription plan is invalid");
        }

        LocalDate today =
                LocalDate.now();

        UserSubscription existingSubscription =
                subscriptionRepository
                        .findByUserIdAndPlanId( user.getId(), plan.getId()
                        )
                        .orElse(null);

        // -----------------------------------------------------
        // ALREADY ACTIVE
        // -----------------------------------------------------

        if (existingSubscription != null &&
                existingSubscription.getStatus() ==
                        SubscriptionStatus.ACTIVE &&
                existingSubscription.getEndDate() != null &&
                !existingSubscription
                        .getEndDate()
                        .isBefore(today)) {

            return;
        }

        int durationDays =
                plan.getDurationDays();

        if (durationDays <= 0) {

            throw new BadRequestException(
                    "Invalid subscription plan duration");
        }

        LocalDate endDate =
                today.plusDays(durationDays);

        // -----------------------------------------------------
        // UPDATE EXISTING SUBSCRIPTION
        // -----------------------------------------------------

        if (existingSubscription != null) {

            existingSubscription.setStartDate( today);

            existingSubscription.setEndDate( endDate);

            existingSubscription.setStatus( SubscriptionStatus.ACTIVE);

            existingSubscription.setActivatedAt( LocalDateTime.now());

            subscriptionRepository.save( existingSubscription);

            return;
        }

        // -----------------------------------------------------
        // CREATE NEW SUBSCRIPTION
        // -----------------------------------------------------

        UserSubscription subscription =
                UserSubscription.builder()
                        .user(user)
                        .plan(plan)
                        .startDate(today)
                        .endDate(endDate)
                        .status( SubscriptionStatus.ACTIVE )
                        .activatedAt( LocalDateTime.now()
                        )
                        .build();

        subscriptionRepository.save( subscription);
    }

    // =========================================================
    // FIND USER
    // =========================================================

    private User findUser(Long id) {

        if (id == null) {

            throw new BadRequestException(
                    "User ID is required");
        }

        return userRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: "
                                        + id
                        ));
    }

    // =========================================================
    // FIND PLAN
    // =========================================================

    private SubscriptionPlan findPlan( Long id) {

        if (id == null) {

            throw new BadRequestException(
                    "Plan ID is required");
        }

        return planRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subscription plan not found with id: "
                                        + id
                        ));
    }
}

