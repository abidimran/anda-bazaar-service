package com.andabazaar.payment;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.andabazaar.config.RazorpayConfig;

@ExtendWith(MockitoExtension.class)
@DisplayName("RazorpayWebhookService Tests")
class RazorpayWebhookServiceTest {

    @Mock
    private RazorpayConfig razorpayConfig;

    @InjectMocks
    private RazorpayWebhookService webhookService;

    @Nested
    @DisplayName("verifyWebhookSignature")
    class VerifyWebhookSignature {

        @Test
        @DisplayName("should return false for null payload")
        void shouldReturnFalseForNullPayload() {
            boolean result = webhookService.verifyWebhookSignature(null, "sig");
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("should return false for blank payload")
        void shouldReturnFalseForBlankPayload() {
            boolean result = webhookService.verifyWebhookSignature("  ", "sig");
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("should return false for null signature")
        void shouldReturnFalseForNullSignature() {
            boolean result = webhookService.verifyWebhookSignature("payload", null);
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("should return false for blank signature")
        void shouldReturnFalseForBlankSignature() {
            boolean result = webhookService.verifyWebhookSignature("payload", "  ");
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("should return false for invalid signature")
        void shouldReturnFalseForInvalidSignature() {
            // verifyWebhookSignature catches the exception and returns false
            boolean result = webhookService.verifyWebhookSignature("payload", "invalid_sig");
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("getEvent")
    class GetEvent {

        @Test
        @DisplayName("should extract event from payload")
        void shouldExtractEvent() {
            String payload = "{\"event\": \"payment.captured\"}";

            String result = webhookService.getEvent(payload);

            assertThat(result).isEqualTo("payment.captured");
        }
    }

    @Nested
    @DisplayName("getRazorpayOrderId")
    class GetRazorpayOrderId {

        @Test
        @DisplayName("should extract order id from payment entity")
        void shouldExtractFromPayment() {
            String payload = """
                {
                  "payload": {
                    "payment": {
                      "entity": {
                        "order_id": "order_123"
                      }
                    }
                  }
                }
                """;

            String result = webhookService.getRazorpayOrderId(payload);
            assertThat(result).isEqualTo("order_123");
        }

        @Test
        @DisplayName("should extract order id from order entity when no payment")
        void shouldExtractFromOrder() {
            String payload = """
                {
                  "payload": {
                    "order": {
                      "entity": {
                        "id": "order_456"
                      }
                    }
                  }
                }
                """;

            String result = webhookService.getRazorpayOrderId(payload);
            assertThat(result).isEqualTo("order_456");
        }

        @Test
        @DisplayName("should return null when no payload object")
        void shouldReturnNullNoPayload() {
            String payload = "{}";
            String result = webhookService.getRazorpayOrderId(payload);
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("should return null when no matching entities")
        void shouldReturnNullNoEntities() {
            String payload = "{\"payload\": {}}";
            String result = webhookService.getRazorpayOrderId(payload);
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("getRazorpayPaymentId")
    class GetRazorpayPaymentId {

        @Test
        @DisplayName("should extract payment id")
        void shouldExtractPaymentId() {
            String payload = """
                {
                  "payload": {
                    "payment": {
                      "entity": {
                        "id": "pay_123"
                      }
                    }
                  }
                }
                """;

            String result = webhookService.getRazorpayPaymentId(payload);
            assertThat(result).isEqualTo("pay_123");
        }

        @Test
        @DisplayName("should return null when no payload")
        void shouldReturnNullNoPayload() {
            String payload = "{}";
            String result = webhookService.getRazorpayPaymentId(payload);
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("should return null when no payment object")
        void shouldReturnNullNoPaymentObject() {
            String payload = "{\"payload\": {}}";
            String result = webhookService.getRazorpayPaymentId(payload);
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("should return null when no entity")
        void shouldReturnNullNoEntity() {
            String payload = "{\"payload\": {\"payment\": {}}}";
            String result = webhookService.getRazorpayPaymentId(payload);
            assertThat(result).isNull();
        }
    }
}
