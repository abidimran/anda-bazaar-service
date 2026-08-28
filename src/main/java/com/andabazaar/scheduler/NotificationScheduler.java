package com.andabazaar.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.entity.Notification;
import com.andabazaar.entity.User;
import com.andabazaar.entity.UserSubscription;
import com.andabazaar.enums.NotificationType;
import com.andabazaar.enums.SubscriptionStatus;
import com.andabazaar.repository.NotificationRepository;
import com.andabazaar.repository.UserRepository;
import com.andabazaar.repository.UserSubscriptionRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final UserSubscriptionRepository subscriptionRepository;

    // SUBSCRIPTION EXPIRY NOTIFICATION

    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void sendSubscriptionExpiryNotifications() {

        LocalDate today = LocalDate.now();

        LocalDate tomorrow = today.plusDays(1);

        List<UserSubscription> subscriptions =
                subscriptionRepository
                        .findByStatusAndEndDateGreaterThanEqual( SubscriptionStatus.ACTIVE, today);

        for (UserSubscription subscription : subscriptions) {

            if (subscription.getEndDate() == null) {
                continue;
            }

            LocalDate endDate =
                    subscription.getEndDate();

            if (endDate.equals(today)) {

                createNotification(
                        subscription.getUser(),
                        NotificationType.SUBSCRIPTION_EXPIRING,
                        "Subscription Expiring Today",
                        "Your "
                                + getPlanName(subscription)
                                + " subscription expires today.");
            }

            else if (endDate.equals(tomorrow)) {

                createNotification(
                        subscription.getUser(),
                        NotificationType.SUBSCRIPTION_EXPIRING,
                        "Subscription Expiring Tomorrow",
                        "Your "
                                + getPlanName(subscription)
                                + " subscription will expire tomorrow.");
            }
        }
    }

    private void createNotification( User user, NotificationType type, String title, String message) {

        Notification notification =
                Notification.builder()
                        .user(user)
                        .type(type)
                        .title(title)
                        .message(message)
                        .read(false)
                        .sent(false)
                        .build();

        notificationRepository.save(notification);
    }

    private String getPlanName( UserSubscription subscription) {

        if (subscription.getPlan() == null) {
            return "subscription";
        }

        if (subscription.getPlan().getName() == null) {
            return "subscription";
        }

        return subscription.getPlan().getName();
    }
}