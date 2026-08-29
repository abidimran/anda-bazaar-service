package com.andabazaar.serviceimpl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.andabazaar.dto.payment.PaymentResponseDto;
import com.andabazaar.dto.payment.PaymentVerificationDto;
import com.andabazaar.repository.entity.Payment;
import com.andabazaar.repository.entity.User;
import com.andabazaar.enums.PaymentStatus;
import com.andabazaar.enums.RoleType;
import com.andabazaar.enums.UserStatus;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.mapper.PaymentMapper;
import com.andabazaar.payment.RazorpayService;
import com.andabazaar.repository.PaymentRepository;
import com.andabazaar.repository.UserRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayException;

@ExtendWith(MockitoExtension.class)
@DisplayName("PaymentServiceImpl Tests")
class PaymentServiceImplTest {

    @Mock private PaymentRepository paymentRepository;
    @Mock private UserRepository userRepository;
    @Mock private RazorpayService razorpayService;
    @Mock private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private User user;
    private Payment payment;
    private PaymentResponseDto responseDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L).firstName("John").lastName("Doe")
                .email("john@test.com").phone("1234567890")
                .password("enc").role(RoleType.USER).status(UserStatus.ACTIVE).build();

        payment = Payment.builder()
                .id(1L).user(user)
                .amount(new BigDecimal("199.00")).currency("INR")
                .razorpayOrderId("order_123").status(PaymentStatus.PENDING).build();

        responseDto = PaymentResponseDto.builder()
                .id(1L).userId(1L).amount(new BigDecimal("199.00"))
                .currency("INR").status(PaymentStatus.PENDING).build();
    }

    @Nested
    @DisplayName("createPayment")
    class CreatePayment {

        @Test
        @DisplayName("should create payment successfully")
        void shouldCreatePayment() throws RazorpayException {
            Order order = mock(Order.class);
            when(order.get("id")).thenReturn("order_123");

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(razorpayService.createOrder(any(), eq("INR"), anyString())).thenReturn(order);
            when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
            when(paymentMapper.toDto(any(Payment.class))).thenReturn(responseDto);

            PaymentResponseDto result = paymentService.createPayment(1L, new BigDecimal("199.00"));

            assertThat(result).isNotNull();
            assertThat(result.getAmount()).isEqualByComparingTo(new BigDecimal("199.00"));
        }

        @Test
        @DisplayName("should throw when amount is null")
        void shouldThrowWhenAmountNull() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            assertThatThrownBy(() -> paymentService.createPayment(1L, null))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Amount must be greater than zero");
        }

        @Test
        @DisplayName("should throw when amount is zero")
        void shouldThrowWhenAmountZero() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            assertThatThrownBy(() -> paymentService.createPayment(1L, BigDecimal.ZERO))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Amount must be greater than zero");
        }

        @Test
        @DisplayName("should throw when Razorpay returns null order ID")
        void shouldThrowWhenNullOrderId() throws RazorpayException {
            Order order = mock(Order.class);
            when(order.get("id")).thenReturn(null);

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(razorpayService.createOrder(any(), eq("INR"), anyString())).thenReturn(order);

            assertThatThrownBy(() -> paymentService.createPayment(1L, new BigDecimal("199.00")))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Razorpay order ID was not generated");
        }

        @Test
        @DisplayName("should throw when Razorpay throws exception")
        void shouldThrowWhenRazorpayFails() throws RazorpayException {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(razorpayService.createOrder(any(), eq("INR"), anyString()))
                    .thenThrow(new RazorpayException("API error"));

            assertThatThrownBy(() -> paymentService.createPayment(1L, new BigDecimal("199.00")))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Unable to create Razorpay order");
        }

        @Test
        @DisplayName("should throw when user not found")
        void shouldThrowWhenUserNotFound() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> paymentService.createPayment(99L, new BigDecimal("199.00")))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("should throw when user ID is null")
        void shouldThrowWhenUserIdNull() {
            assertThatThrownBy(() -> paymentService.createPayment(null, new BigDecimal("199.00")))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("User ID is required");
        }
    }

    @Nested
    @DisplayName("verifyPayment")
    class VerifyPayment {

        @Test
        @DisplayName("should verify payment successfully")
        void shouldVerifyPayment() throws RazorpayException {
            PaymentVerificationDto verification = PaymentVerificationDto.builder()
                    .razorpayOrderId("order_123").razorpayPaymentId("pay_123")
                    .razorpaySignature("sig_123").build();

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(paymentRepository.findByRazorpayOrderId("order_123")).thenReturn(Optional.of(payment));
            when(razorpayService.verifySignature("order_123", "pay_123", "sig_123")).thenReturn(true);
            when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
            when(paymentMapper.toDto(any(Payment.class))).thenReturn(responseDto);

            PaymentResponseDto result = paymentService.verifyPayment(1L, verification);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("should throw when verification data is null")
        void shouldThrowWhenNullData() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            assertThatThrownBy(() -> paymentService.verifyPayment(1L, null))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Payment verification data is required");
        }

        @Test
        @DisplayName("should throw when order ID is blank")
        void shouldThrowWhenOrderIdBlank() {
            PaymentVerificationDto v = PaymentVerificationDto.builder()
                    .razorpayOrderId("").razorpayPaymentId("pay").razorpaySignature("sig").build();
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            assertThatThrownBy(() -> paymentService.verifyPayment(1L, v))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Razorpay order ID is required");
        }

        @Test
        @DisplayName("should throw when payment ID is blank")
        void shouldThrowWhenPaymentIdBlank() {
            PaymentVerificationDto v = PaymentVerificationDto.builder()
                    .razorpayOrderId("order").razorpayPaymentId("").razorpaySignature("sig").build();
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            assertThatThrownBy(() -> paymentService.verifyPayment(1L, v))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Razorpay payment ID is required");
        }

        @Test
        @DisplayName("should throw when signature is blank")
        void shouldThrowWhenSignatureBlank() {
            PaymentVerificationDto v = PaymentVerificationDto.builder()
                    .razorpayOrderId("order").razorpayPaymentId("pay").razorpaySignature("").build();
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            assertThatThrownBy(() -> paymentService.verifyPayment(1L, v))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Razorpay signature is required");
        }

        @Test
        @DisplayName("should throw when payment does not belong to user")
        void shouldThrowWhenPaymentNotBelongsToUser() {
            User otherUser = User.builder().id(99L).build();
            Payment otherPayment = Payment.builder()
                    .id(2L).user(otherUser)
                    .amount(new BigDecimal("199.00")).status(PaymentStatus.PENDING)
                    .razorpayOrderId("order_456").build();

            PaymentVerificationDto v = PaymentVerificationDto.builder()
                    .razorpayOrderId("order_456").razorpayPaymentId("pay").razorpaySignature("sig").build();

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(paymentRepository.findByRazorpayOrderId("order_456")).thenReturn(Optional.of(otherPayment));

            assertThatThrownBy(() -> paymentService.verifyPayment(1L, v))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Payment does not belong to this user");
        }

        @Test
        @DisplayName("should return already successful payment")
        void shouldReturnAlreadySuccessfulPayment() throws RazorpayException {
            payment.setStatus(PaymentStatus.SUCCESS);
            PaymentVerificationDto v = PaymentVerificationDto.builder()
                    .razorpayOrderId("order_123").razorpayPaymentId("pay").razorpaySignature("sig").build();

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(paymentRepository.findByRazorpayOrderId("order_123")).thenReturn(Optional.of(payment));
            when(paymentMapper.toDto(payment)).thenReturn(responseDto);

            PaymentResponseDto result = paymentService.verifyPayment(1L, v);

            assertThat(result).isNotNull();
            verify(razorpayService, never()).verifySignature(any(), any(), any());
        }

        @Test
        @DisplayName("should throw when signature verification fails")
        void shouldThrowWhenSignatureFails() throws RazorpayException {
            PaymentVerificationDto v = PaymentVerificationDto.builder()
                    .razorpayOrderId("order_123").razorpayPaymentId("pay").razorpaySignature("bad_sig").build();

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(paymentRepository.findByRazorpayOrderId("order_123")).thenReturn(Optional.of(payment));
            when(razorpayService.verifySignature("order_123", "pay", "bad_sig")).thenReturn(false);
            when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

            assertThatThrownBy(() -> paymentService.verifyPayment(1L, v))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Payment verification failed");
        }

        @Test
        @DisplayName("should throw when Razorpay throws during verification")
        void shouldThrowWhenRazorpayThrows() throws RazorpayException {
            PaymentVerificationDto v = PaymentVerificationDto.builder()
                    .razorpayOrderId("order_123").razorpayPaymentId("pay").razorpaySignature("sig").build();

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(paymentRepository.findByRazorpayOrderId("order_123")).thenReturn(Optional.of(payment));
            when(razorpayService.verifySignature("order_123", "pay", "sig"))
                    .thenThrow(new RazorpayException("error"));
            when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

            assertThatThrownBy(() -> paymentService.verifyPayment(1L, v))
                    .isInstanceOf(BadRequestException.class);
        }
    }

    @Nested
    @DisplayName("getPaymentById")
    class GetPaymentById {

        @Test
        @DisplayName("should return payment by id")
        void shouldReturnById() {
            when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
            when(paymentMapper.toDto(payment)).thenReturn(responseDto);

            PaymentResponseDto result = paymentService.getPaymentById(1L);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(paymentRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> paymentService.getPaymentById(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getUserPayments")
    class GetUserPayments {

        @Test
        @DisplayName("should return user payments")
        void shouldReturnUserPayments() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(paymentRepository.findByUserIdOrderByCreatedAtDesc(1L)).thenReturn(List.of(payment));
            when(paymentMapper.toDto(payment)).thenReturn(responseDto);

            List<PaymentResponseDto> result = paymentService.getUserPayments(1L);

            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("getPaymentByTransactionId")
    class GetPaymentByTransactionId {

        @Test
        @DisplayName("should return payment by transaction id")
        void shouldReturn() {
            when(paymentRepository.findByTransactionId("txn_123")).thenReturn(Optional.of(payment));
            when(paymentMapper.toDto(payment)).thenReturn(responseDto);

            PaymentResponseDto result = paymentService.getPaymentByTransactionId("txn_123");

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("should throw when transaction id is blank")
        void shouldThrowWhenBlank() {
            assertThatThrownBy(() -> paymentService.getPaymentByTransactionId("  "))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Transaction ID is required");
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(paymentRepository.findByTransactionId("unknown")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> paymentService.getPaymentByTransactionId("unknown"))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("processRazorpayWebhook")
    class ProcessWebhook {

        @Test
        @DisplayName("should handle null payload")
        void shouldHandleNullPayload() {
            paymentService.processRazorpayWebhook(null);
            // no exception
        }

        @Test
        @DisplayName("should handle blank payload")
        void shouldHandleBlankPayload() {
            paymentService.processRazorpayWebhook("  ");
            // no exception
        }

        @Test
        @DisplayName("should handle payment.captured event")
        void shouldHandlePaymentCaptured() {
            String payload = """
                {
                  "event": "payment.captured",
                  "payload": {
                    "payment": {
                      "entity": {
                        "id": "pay_123",
                        "order_id": "order_123"
                      }
                    }
                  }
                }
                """;

            when(paymentRepository.findByRazorpayOrderId("order_123")).thenReturn(Optional.of(payment));
            when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

            paymentService.processRazorpayWebhook(payload);

            verify(paymentRepository).save(argThat(p -> p.getStatus() == PaymentStatus.SUCCESS));
        }

        @Test
        @DisplayName("should skip already successful payment on captured event")
        void shouldSkipAlreadySuccessfulOnCaptured() {
            payment.setStatus(PaymentStatus.SUCCESS);
            String payload = """
                {
                  "event": "payment.captured",
                  "payload": {
                    "payment": {
                      "entity": {
                        "id": "pay_123",
                        "order_id": "order_123"
                      }
                    }
                  }
                }
                """;

            when(paymentRepository.findByRazorpayOrderId("order_123")).thenReturn(Optional.of(payment));

            paymentService.processRazorpayWebhook(payload);

            verify(paymentRepository, never()).save(any());
        }

        @Test
        @DisplayName("should handle payment.failed event")
        void shouldHandlePaymentFailed() {
            String payload = """
                {
                  "event": "payment.failed",
                  "payload": {
                    "payment": {
                      "entity": {
                        "id": "pay_123",
                        "order_id": "order_123",
                        "error_description": "Card declined"
                      }
                    }
                  }
                }
                """;

            when(paymentRepository.findByRazorpayOrderId("order_123")).thenReturn(Optional.of(payment));
            when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

            paymentService.processRazorpayWebhook(payload);

            verify(paymentRepository).save(argThat(p -> p.getStatus() == PaymentStatus.FAILED));
        }

        @Test
        @DisplayName("should skip null payload object")
        void shouldSkipNullPayloadObject() {
            String payload = "{\"event\": \"payment.captured\"}";

            paymentService.processRazorpayWebhook(payload);

            verify(paymentRepository, never()).findByRazorpayOrderId(any());
        }

        @Test
        @DisplayName("should skip null payment object")
        void shouldSkipNullPaymentObject() {
            String payload = "{\"event\": \"payment.captured\", \"payload\": {}}";

            paymentService.processRazorpayWebhook(payload);

            verify(paymentRepository, never()).findByRazorpayOrderId(any());
        }

        @Test
        @DisplayName("should skip null entity object")
        void shouldSkipNullEntity() {
            String payload = "{\"event\": \"payment.captured\", \"payload\": {\"payment\": {}}}";

            paymentService.processRazorpayWebhook(payload);

            verify(paymentRepository, never()).findByRazorpayOrderId(any());
        }

        @Test
        @DisplayName("should skip when payment not found by order id")
        void shouldSkipWhenPaymentNotFound() {
            String payload = """
                {
                  "event": "payment.captured",
                  "payload": {
                    "payment": {
                      "entity": {
                        "id": "pay_123",
                        "order_id": "order_unknown"
                      }
                    }
                  }
                }
                """;

            when(paymentRepository.findByRazorpayOrderId("order_unknown")).thenReturn(Optional.empty());

            paymentService.processRazorpayWebhook(payload);

            verify(paymentRepository, never()).save(any());
        }

        @Test
        @DisplayName("should throw on invalid JSON")
        void shouldThrowOnInvalidJson() {
            assertThatThrownBy(() -> paymentService.processRazorpayWebhook("not json"))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("Unable to process Razorpay webhook");
        }
    }
}
