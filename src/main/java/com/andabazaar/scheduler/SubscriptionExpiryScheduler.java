package com.andabazaar.scheduler;

import java.time.LocalDate;
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
public class SubscriptionExpiryScheduler {

    private final UserSubscriptionRepository
            subscriptionRepository;

    private final NotificationService
            notificationService;

    private final PushNotificationService
            pushNotificationService;

    private final NotificationTemplateService
            notificationTemplateService;

    /*
     * Runs and check every per hour.
     */
    @Scheduled(fixedRate = 3600000)
    public void expireSubscriptions() {

        LocalDate today = LocalDate.now();

        List<UserSubscription> subscriptions =
                subscriptionRepository
                    .findByStatusAndEndDateLessThan(
                        SubscriptionStatus.ACTIVE,
                        today
                    );

        for (UserSubscription subscription :
                subscriptions) {

            subscription.setStatus(
                    SubscriptionStatus.EXPIRED
            );

            Long userId =
                    subscription.getUser().getId();

            String title =
                    "Subscription Expired";

            String message =
                    notificationTemplateService
                        .subscriptionExpired();

            NotificationRequestDto request =
                    NotificationRequestDto.builder()
                            .userId(userId)
                            .type(
                                NotificationType
                                    .SUBSCRIPTION_EXPIRED
                            )
                            .title(title)
                            .message(message)
                            .build();

            notificationService
                    .createNotification(request);

            pushNotificationService
                    .sendNotification(
                            userId,
                            title,
                            message
                    );
        }

        subscriptionRepository.saveAll(
                subscriptions
        );
    }
}