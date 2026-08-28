package com.andabazaar.mapper;

import org.springframework.stereotype.Component;

import com.andabazaar.dto.notification.NotificationResponseDto;
import com.andabazaar.entity.Notification;

@Component
public class NotificationMapper {

    public NotificationResponseDto toDto(
            Notification notification) {

        if (notification == null) {
            return null;
        }

        return NotificationResponseDto.builder()
                .id(notification.getId())
                .userId(
                        notification.getUser() != null
                                ? notification.getUser().getId()
                                : null
                )
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .read(notification.getRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}