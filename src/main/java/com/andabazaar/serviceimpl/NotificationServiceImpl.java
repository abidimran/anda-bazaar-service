package com.andabazaar.serviceimpl;

import com.andabazaar.dto.notification.NotificationRequestDto;
import com.andabazaar.dto.notification.NotificationResponseDto;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.NotificationRepository;
import com.andabazaar.repository.UserRepository;
import com.andabazaar.repository.entity.Notification;
import com.andabazaar.repository.entity.User;
import com.andabazaar.service.NotificationService;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl
        implements NotificationService {
    private final NotificationRepository
            notificationRepository;
    private final UserRepository userRepository;

    @Override
    public NotificationResponseDto createNotification(NotificationRequestDto request) {
        User user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Notification notification =
                Notification.builder()
                        .user(user)
                        .type(request.getType())
                        .title(request.getTitle())
                        .message(request.getMessage())
                        .read(false)
                        .sent(false)
                        .build();
        return mapToResponse( notificationRepository.save(notification));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponseDto>
            getUserNotifications(Long userId) {
        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponseDto>
            getUnreadNotifications(Long userId) {
        return notificationRepository
                .findByUserIdAndReadFalseOrderByCreatedAtDesc( userId )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        return notificationRepository
                .countByUserIdAndReadFalse(userId);
    }

    @Override
    public void markAsRead(Long notificationId, Long userId) {
        Notification notification =
                notificationRepository
                        .findById(notificationId)
                        .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        if (!notification.getUser()
                .getId()
                .equals(userId)) {
            throw new ResourceNotFoundException("Notification not found");
        }

        notification.setRead(true);
        notification.setReadAt(LocalDateTime.now());
    }

    @Override
    public void markAllAsRead(Long userId) {
        List<Notification> notifications = notificationRepository
                    .findByUserIdAndReadFalseOrderByCreatedAtDesc( userId);
        LocalDateTime now = LocalDateTime.now();
        for (Notification notification : notifications) {
            notification.setRead(true);
            notification.setReadAt(now);
        }
    }

    @Override
    public void deleteNotification(Long notificationId, Long userId) {
        Notification notification =
                notificationRepository
                    .findById(notificationId)
                    .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        if (!notification.getUser()
                .getId()
                .equals(userId)) {
            throw new ResourceNotFoundException("Notification not found");
        }

        notificationRepository.delete(notification);
    }

    private NotificationResponseDto
            mapToResponse(Notification notification) {
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .userId( notification.getUser().getId() )
                .type(notification.getType())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .read(notification.getRead())
                .sent(notification.getSent())
                .createdAt( notification.getCreatedAt() )
                .readAt(notification.getReadAt())
                .sentAt(notification.getSentAt())
                .build();
    }
}
