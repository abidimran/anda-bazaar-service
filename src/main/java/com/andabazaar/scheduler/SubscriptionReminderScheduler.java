package com.andabazaar.scheduler;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.andabazaar.dto.notification.NotificationRequestDto;
import com.andabazaar.entity.UserSubscription;
import com.andabazaar.enums.NotificationType;
import com.andabazaar.enums.SubscriptionStatus;
import com.andabazaar.notification.NotificationTemplateService;
import com.andabazaar.notification.PushNotificationService;
import com.andabazaar.repository.UserSubscriptionRepository;
import com.andabazaar.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubscriptionReminderScheduler {

    private final UserSubscriptionRepository subscriptionRepository;

    private final NotificationService notificationService;

    private final PushNotificationService pushNotificationService;

    private final NotificationTemplateService notificationTemplateService;

    /*
     * Runs every day at 9:00 AM.
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void sendExpiryReminders() {

        LocalDate today = LocalDate.now();

        // Get only ACTIVE subscriptions
        // whose end date is today or in the future
        List<UserSubscription> subscriptions =
                subscriptionRepository
                        .findByStatusAndEndDateGreaterThanEqual( SubscriptionStatus.ACTIVE, today);

        for (UserSubscription subscription : subscriptions) {

            long daysRemaining =
                    ChronoUnit.DAYS.between( today, subscription.getEndDate());

            // Reminder at 3, 2 and 1 day before expiry
            if (daysRemaining == 3
                    || daysRemaining == 2
                    || daysRemaining == 1) {

                sendExpiryNotification( subscription, daysRemaining);
            }
        }
    }

    private void sendExpiryNotification( UserSubscription subscription, long daysRemaining) {

        Long userId =
                subscription.getUser().getId();

        String title =
                "Subscription Expiring Soon";

        String message =
                notificationTemplateService
                        .subscriptionExpiring( daysRemaining);

        NotificationRequestDto request =
                NotificationRequestDto.builder()
                        .userId(userId)
                        .type( NotificationType .SUBSCRIPTION_EXPIRING )
                        .title(title)
                        .message(message)
                        .build();

        notificationService
                .createNotification(request);

        pushNotificationService
                .sendNotification( userId, title, message);
    }
}