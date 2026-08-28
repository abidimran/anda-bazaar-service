
package com.andabazaar.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.payment.RazorpayWebhookService;
import com.andabazaar.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments/webhook")
@RequiredArgsConstructor
public class RazorpayWebhookController {

    private final RazorpayWebhookService webhookService;

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
            @RequestHeader(
                    value = "X-Razorpay-Signature",
                    required = false
            ) String signature) {

        boolean valid =
                webhookService.verifyWebhookSignature( payload, signature);

        if (!valid) {
 return ResponseEntity.badRequest().body("Invalid webhook signature");
        }

        paymentService.processRazorpayWebhook( payload);

 return ResponseEntity.ok("Webhook processed");
    }
}

