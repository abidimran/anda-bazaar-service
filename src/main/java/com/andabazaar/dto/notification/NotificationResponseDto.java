package com.andabazaar.dto.notification;

import com.andabazaar.enums.NotificationType;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDto {
    private Long id;

    private Long userId;

    private NotificationType type;

    private String title;

    private String message;

    private Boolean read;

    private Boolean sent;

    private LocalDateTime createdAt;

    private LocalDateTime readAt;

    private LocalDateTime sentAt;
}
