
package com.andabazaar.controller;

import com.andabazaar.payment.RazorpayWebhookService;
import com.andabazaar.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "RazorPay Webhooks", description = "Razorpay payment webhook handler")
@RestController
@RequestMapping("/api/payments/webhook")
@RequiredArgsConstructor
public class RazorpayWebhookController {
    private final RazorpayWebhookService webhookService;

    private final PaymentService paymentService;

    @Operation(summary = "Handle Webhook")
    @PostMapping
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
            @RequestHeader(
                    value = "X-Razorpay-Signature",
                    required = false
            ) String signature) {
        boolean valid = webhookService.verifyWebhookSignature( payload, signature);
        if (!valid) {
 return ResponseEntity.badRequest().body("Invalid webhook signature");
        }

        paymentService.processRazorpayWebhook( payload);
 return ResponseEntity.ok("Webhook processed");
    }
}
