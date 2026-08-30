package com.andabazaar.service;

import com.andabazaar.dto.notification.NotificationRequestDto;
import com.andabazaar.dto.notification.NotificationResponseDto;

import java.util.List;

public interface NotificationService {
    NotificationResponseDto createNotification(NotificationRequestDto request);

    List<NotificationResponseDto> getUserNotifications(Long userId);

    List<NotificationResponseDto> getUnreadNotifications(Long userId);

    long getUnreadCount(Long userId);

    void markAsRead(Long notificationId, Long userId);

    void markAllAsRead(Long userId);

    void deleteNotification(Long notificationId, Long userId);
}
