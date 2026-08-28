package com.andabazaar.notification;

import org.springframework.stereotype.Service;

@Service
public class NotificationTemplateService {

    public String subscriptionExpiring(
            long daysRemaining) {

        return "Your Anda Bazaar subscription "
                + "will expire in "
                + daysRemaining
                + " days. Renew now to continue "
                + "accessing today's and yesterday's "
                + "egg prices.";
    }

    public String subscriptionExpired() {

        return "Your Anda Bazaar subscription has "
                + "expired. You can renew your plan "
                + "to access today's and yesterday's "
                + "egg prices.";
    }

    public String paymentSuccess() {

        return "Your payment was successful. "
                + "Your Anda Bazaar subscription is now active.";
    }

    public String paymentFailed() {

        return "Your payment could not be verified. "
                + "Please try again.";
    }
}