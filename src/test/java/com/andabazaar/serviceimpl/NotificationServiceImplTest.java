package com.andabazaar.serviceimpl;

import com.andabazaar.dto.notification.NotificationRequestDto;
import com.andabazaar.dto.notification.NotificationResponseDto;
import com.andabazaar.enums.NotificationType;
import com.andabazaar.enums.RoleType;
import com.andabazaar.enums.UserStatus;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.NotificationRepository;
import com.andabazaar.repository.UserRepository;
import com.andabazaar.repository.entity.Notification;
import com.andabazaar.repository.entity.User;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationServiceImpl Tests")
class NotificationServiceImplTest {
    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private User user;
    private Notification notification;
    private NotificationRequestDto requestDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L).firstName("John").lastName("Doe")
                .email("john@example.com").phone("1234567890")
                .password("encoded").role(RoleType.USER).status(UserStatus.ACTIVE)
                .build();
        notification = Notification.builder()
                .id(1L).user(user).type(NotificationType.GENERAL)
                .title("Test").message("Test message")
                .read(false).sent(false).build();
        requestDto = NotificationRequestDto.builder()
                .userId(1L).type(NotificationType.GENERAL)
                .title("Test").message("Test message").build();
    }

    @Nested
    @DisplayName("createNotification")
    class CreateNotification {
        @Test
        @DisplayName("should create notification successfully")
        void shouldCreateNotification() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
            NotificationResponseDto result = notificationService.createNotification(requestDto);
            assertThat(result).isNotNull();
            assertThat(result.getTitle()).isEqualTo("Test");
            assertThat(result.getRead()).isFalse();
        }

        @Test
        @DisplayName("should throw when user not found")
        void shouldThrowWhenUserNotFound() {
            when(userRepository.findById(1L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> notificationService.createNotification(requestDto))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("User not found");
        }
    }

    @Nested
    @DisplayName("getUserNotifications")
    class GetUserNotifications {
        @Test
        @DisplayName("should return user notifications")
        void shouldReturnUserNotifications() {
            when(notificationRepository.findByUserIdOrderByCreatedAtDesc(1L))
                    .thenReturn(List.of(notification));
            List<NotificationResponseDto> result = notificationService.getUserNotifications(1L);
            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("getUnreadNotifications")
    class GetUnreadNotifications {
        @Test
        @DisplayName("should return unread notifications")
        void shouldReturnUnreadNotifications() {
            when(notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(1L))
                    .thenReturn(List.of(notification));
            List<NotificationResponseDto> result = notificationService.getUnreadNotifications(1L);
            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("getUnreadCount")
    class GetUnreadCount {
        @Test
        @DisplayName("should return unread count")
        void shouldReturnUnreadCount() {
            when(notificationRepository.countByUserIdAndReadFalse(1L)).thenReturn(5L);
            long result = notificationService.getUnreadCount(1L);
            assertThat(result).isEqualTo(5L);
        }
    }

    @Nested
    @DisplayName("markAsRead")
    class MarkAsRead {
        @Test
        @DisplayName("should mark notification as read")
        void shouldMarkAsRead() {
            when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
            notificationService.markAsRead(1L, 1L);
            assertThat(notification.getRead()).isTrue();
            assertThat(notification.getReadAt()).isNotNull();
        }

        @Test
        @DisplayName("should throw when notification not found")
        void shouldThrowWhenNotFound() {
            when(notificationRepository.findById(99L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> notificationService.markAsRead(99L, 1L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("should throw when userId does not match")
        void shouldThrowWhenUserIdMismatch() {
            when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
            assertThatThrownBy(() -> notificationService.markAsRead(1L, 999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Notification not found");
        }
    }

    @Nested
    @DisplayName("markAllAsRead")
    class MarkAllAsRead {
        @Test
        @DisplayName("should mark all unread notifications as read")
        void shouldMarkAllAsRead() {
            Notification n2 = Notification.builder()
                    .id(2L).user(user).type(NotificationType.SYSTEM)
                    .title("Test2").message("Message2").read(false).sent(false).build();
            when(notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(1L))
                    .thenReturn(List.of(notification, n2));
            notificationService.markAllAsRead(1L);
            assertThat(notification.getRead()).isTrue();
            assertThat(n2.getRead()).isTrue();
        }

        @Test
        @DisplayName("should handle empty list gracefully")
        void shouldHandleEmptyList() {
            when(notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(1L))
                    .thenReturn(List.of());
            notificationService.markAllAsRead(1L);
            // no exception thrown
        }
    }

    @Nested
    @DisplayName("deleteNotification")
    class DeleteNotification {
        @Test
        @DisplayName("should delete notification")
        void shouldDeleteNotification() {
            when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
            notificationService.deleteNotification(1L, 1L);
            verify(notificationRepository).delete(notification);
        }

        @Test
        @DisplayName("should throw when notification not found")
        void shouldThrowWhenNotFound() {
            when(notificationRepository.findById(99L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> notificationService.deleteNotification(99L, 1L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("should throw when userId does not match")
        void shouldThrowWhenUserIdMismatch() {
            when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
            assertThatThrownBy(() -> notificationService.deleteNotification(1L, 999L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}
