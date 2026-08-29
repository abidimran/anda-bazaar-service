package com.andabazaar.scheduler;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.andabazaar.repository.entity.Notification;
import com.andabazaar.repository.entity.SubscriptionPlan;
import com.andabazaar.repository.entity.User;
import com.andabazaar.repository.entity.UserSubscription;
import com.andabazaar.enums.RoleType;
import com.andabazaar.enums.SubscriptionStatus;
import com.andabazaar.enums.UserStatus;
import com.andabazaar.repository.NotificationRepository;
import com.andabazaar.repository.UserRepository;
import com.andabazaar.repository.UserSubscriptionRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationScheduler Tests")
class NotificationSchedulerTest {

    @Mock private UserRepository userRepository;
    @Mock private NotificationRepository notificationRepository;
    @Mock private UserSubscriptionRepository subscriptionRepository;

    @InjectMocks
    private NotificationScheduler scheduler;

    private User user;
    private SubscriptionPlan plan;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L).firstName("John").lastName("Doe")
                .email("john@test.com").phone("1234567890")
                .password("enc").role(RoleType.USER).status(UserStatus.ACTIVE).build();

        plan = SubscriptionPlan.builder().id(1L).name("Premium").build();
    }

    @Nested
    @DisplayName("sendSubscriptionExpiryNotifications")
    class SendExpiryNotifications {

        @Test
        @DisplayName("should create notification for subscription expiring today")
        void shouldNotifyExpiringToday() {
            UserSubscription sub = UserSubscription.builder()
                    .id(1L).user(user).plan(plan).status(SubscriptionStatus.ACTIVE)
                    .startDate(LocalDate.now().minusDays(30)).endDate(LocalDate.now()).build();

            when(subscriptionRepository.findByStatusAndEndDateGreaterThanEqual(
                    eq(SubscriptionStatus.ACTIVE), any(LocalDate.class)))
                    .thenReturn(List.of(sub));
            when(notificationRepository.save(any(Notification.class))).thenReturn(null);

            scheduler.sendSubscriptionExpiryNotifications();

            verify(notificationRepository).save(argThat(n ->
                    n.getTitle().contains("Today")));
        }

        @Test
        @DisplayName("should create notification for subscription expiring tomorrow")
        void shouldNotifyExpiringTomorrow() {
            UserSubscription sub = UserSubscription.builder()
                    .id(1L).user(user).plan(plan).status(SubscriptionStatus.ACTIVE)
                    .startDate(LocalDate.now().minusDays(29)).endDate(LocalDate.now().plusDays(1)).build();

            when(subscriptionRepository.findByStatusAndEndDateGreaterThanEqual(
                    eq(SubscriptionStatus.ACTIVE), any(LocalDate.class)))
                    .thenReturn(List.of(sub));
            when(notificationRepository.save(any(Notification.class))).thenReturn(null);

            scheduler.sendSubscriptionExpiryNotifications();

            verify(notificationRepository).save(argThat(n ->
                    n.getTitle().contains("Tomorrow")));
        }

        @Test
        @DisplayName("should skip when end date is null")
        void shouldSkipNullEndDate() {
            UserSubscription sub = UserSubscription.builder()
                    .id(1L).user(user).plan(plan).status(SubscriptionStatus.ACTIVE)
                    .startDate(LocalDate.now()).endDate(null).build();

            when(subscriptionRepository.findByStatusAndEndDateGreaterThanEqual(
                    eq(SubscriptionStatus.ACTIVE), any(LocalDate.class)))
                    .thenReturn(List.of(sub));

            scheduler.sendSubscriptionExpiryNotifications();

            verify(notificationRepository, never()).save(any());
        }

        @Test
        @DisplayName("should not notify for subscription ending in 3 days")
        void shouldNotNotifyForFutureSubscription() {
            UserSubscription sub = UserSubscription.builder()
                    .id(1L).user(user).plan(plan).status(SubscriptionStatus.ACTIVE)
                    .startDate(LocalDate.now().minusDays(27)).endDate(LocalDate.now().plusDays(3)).build();

            when(subscriptionRepository.findByStatusAndEndDateGreaterThanEqual(
                    eq(SubscriptionStatus.ACTIVE), any(LocalDate.class)))
                    .thenReturn(List.of(sub));

            scheduler.sendSubscriptionExpiryNotifications();

            verify(notificationRepository, never()).save(any());
        }

        @Test
        @DisplayName("should handle empty subscriptions list")
        void shouldHandleEmptyList() {
            when(subscriptionRepository.findByStatusAndEndDateGreaterThanEqual(
                    eq(SubscriptionStatus.ACTIVE), any(LocalDate.class)))
                    .thenReturn(Collections.emptyList());

            scheduler.sendSubscriptionExpiryNotifications();

            verify(notificationRepository, never()).save(any());
        }

        @Test
        @DisplayName("should handle null plan name")
        void shouldHandleNullPlanName() {
            SubscriptionPlan nullNamePlan = SubscriptionPlan.builder().id(2L).name(null).build();
            UserSubscription sub = UserSubscription.builder()
                    .id(1L).user(user).plan(nullNamePlan).status(SubscriptionStatus.ACTIVE)
                    .startDate(LocalDate.now().minusDays(30)).endDate(LocalDate.now()).build();

            when(subscriptionRepository.findByStatusAndEndDateGreaterThanEqual(
                    eq(SubscriptionStatus.ACTIVE), any(LocalDate.class)))
                    .thenReturn(List.of(sub));
            when(notificationRepository.save(any(Notification.class))).thenReturn(null);

            scheduler.sendSubscriptionExpiryNotifications();

            verify(notificationRepository).save(argThat(n ->
                    n.getMessage().contains("subscription")));
        }

        @Test
        @DisplayName("should handle null plan")
        void shouldHandleNullPlan() {
            UserSubscription sub = UserSubscription.builder()
                    .id(1L).user(user).plan(null).status(SubscriptionStatus.ACTIVE)
                    .startDate(LocalDate.now().minusDays(30)).endDate(LocalDate.now()).build();

            when(subscriptionRepository.findByStatusAndEndDateGreaterThanEqual(
                    eq(SubscriptionStatus.ACTIVE), any(LocalDate.class)))
                    .thenReturn(List.of(sub));
            when(notificationRepository.save(any(Notification.class))).thenReturn(null);

            scheduler.sendSubscriptionExpiryNotifications();

            verify(notificationRepository).save(argThat(n ->
                    n.getMessage().contains("subscription")));
        }
    }
}
