package com.andabazaar.payment;

public interface PaymentWebhookService {

    void processNotification(
            String notificationData);
}