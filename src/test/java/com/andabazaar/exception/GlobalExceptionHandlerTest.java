package com.andabazaar.exception;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Nested
    @DisplayName("handleNotFound")
    class HandleNotFound {

        @Test
        @DisplayName("should return 404 with message")
        void shouldReturn404() {
            ResourceNotFoundException ex = new ResourceNotFoundException("User not found");

            ResponseEntity<Map<String, Object>> response = handler.handleNotFound(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).containsEntry("status", 404);
            assertThat(response.getBody()).containsEntry("message", "User not found");
            assertThat(response.getBody()).containsKey("timestamp");
        }
    }

    @Nested
    @DisplayName("handleBadRequest")
    class HandleBadRequest {

        @Test
        @DisplayName("should return 400 with message")
        void shouldReturn400() {
            BadRequestException ex = new BadRequestException("Invalid input");

            ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).containsEntry("message", "Invalid input");
        }
    }

    @Nested
    @DisplayName("handleUnauthorized")
    class HandleUnauthorized {

        @Test
        @DisplayName("should return 401 with message")
        void shouldReturn401() {
            UnauthorizedException ex = new UnauthorizedException("Unauthorized access");

            ResponseEntity<Map<String, Object>> response = handler.handleUnauthorized(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(response.getBody()).containsEntry("message", "Unauthorized access");
        }
    }

    @Nested
    @DisplayName("handleForbidden")
    class HandleForbidden {

        @Test
        @DisplayName("should return 403 with message")
        void shouldReturn403() {
            ForbiddenException ex = new ForbiddenException("Access denied");

            ResponseEntity<Map<String, Object>> response = handler.handleForbidden(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            assertThat(response.getBody()).containsEntry("message", "Access denied");
        }
    }

    @Nested
    @DisplayName("handlePaymentException")
    class HandlePaymentException {

        @Test
        @DisplayName("should return 400 for payment exception")
        void shouldReturn400ForPayment() {
            PaymentException ex = new PaymentException("Payment failed");

            ResponseEntity<Map<String, Object>> response = handler.handlePaymentException(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).containsEntry("message", "Payment failed");
        }
    }

    @Nested
    @DisplayName("handleSubscriptionException")
    class HandleSubscriptionException {

        @Test
        @DisplayName("should return 400 for subscription exception")
        void shouldReturn400ForSubscription() {
            SubscriptionException ex = new SubscriptionException("Subscription expired");

            ResponseEntity<Map<String, Object>> response = handler.handleSubscriptionException(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).containsEntry("message", "Subscription expired");
        }
    }

    @Nested
    @DisplayName("handleValidation")
    class HandleValidation {

        @Test
        @DisplayName("should return 400 with field errors")
        void shouldReturn400WithFieldErrors() {
            BindingResult bindingResult = mock(BindingResult.class);
            FieldError fieldError = new FieldError("user", "email", "Email is required");
            when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

            MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

            ResponseEntity<Map<String, Object>> response = handler.handleValidation(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).containsEntry("message", "Validation failed");
            assertThat(response.getBody()).containsKey("errors");

            @SuppressWarnings("unchecked")
            Map<String, Object> errors = (Map<String, Object>) response.getBody().get("errors");
            assertThat(errors).containsEntry("email", "Email is required");
        }
    }

    @Nested
    @DisplayName("handleGeneralException")
    class HandleGeneralException {

        @Test
        @DisplayName("should return 500 with generic message")
        void shouldReturn500() {
            Exception ex = new Exception("Something unexpected");

            ResponseEntity<Map<String, Object>> response = handler.handleGeneralException(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(response.getBody()).containsEntry("message", "Something went wrong");
        }
    }
}
