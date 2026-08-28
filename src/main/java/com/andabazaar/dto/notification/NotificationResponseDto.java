package com.andabazaar.dto.notification;

import java.time.LocalDateTime;

import com.andabazaar.enums.NotificationType;

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