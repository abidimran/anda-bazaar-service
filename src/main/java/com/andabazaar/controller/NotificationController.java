package com.andabazaar.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.notification.NotificationRequestDto;
import com.andabazaar.dto.notification.NotificationResponseDto;
import com.andabazaar.service.NotificationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Notifications", description = "User notification management")
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Create Notification")
    @PostMapping
    public ResponseEntity<NotificationResponseDto>
            createNotification(@Valid @RequestBody NotificationRequestDto request) {

 return ResponseEntity.ok(notificationService.createNotification(request));
    }

    @Operation(summary = "Get User Notifications")
    @GetMapping("/user/{userId}")
    public ResponseEntity<
            List<NotificationResponseDto>>
            getUserNotifications(@PathVariable Long userId) {

 return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    @Operation(summary = "Get Unread Notifications")
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<
            List<NotificationResponseDto>>
            getUnreadNotifications(@PathVariable Long userId) {

 return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }

    @Operation(summary = "Get Unread Count")
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long>
            getUnreadCount(@PathVariable Long userId) {

 return ResponseEntity.ok(notificationService.getUnreadCount(userId));
    }

    @Operation(summary = "Mark As Read")
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Void>
            markAsRead(@PathVariable Long notificationId, @RequestParam Long userId) {

        notificationService.markAsRead( notificationId, userId);

 return ResponseEntity.ok().build();
    }

    @Operation(summary = "Mark All As Read")
    @PutMapping("/user/{userId}/read-status")
    public ResponseEntity<Void>
            markAllAsRead(@PathVariable Long userId) {

        notificationService
                .markAllAsRead(userId);

 return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete Notification")
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void>
            deleteNotification(@PathVariable Long notificationId, @RequestParam Long userId) {

        notificationService.deleteNotification( notificationId, userId);

 return ResponseEntity.noContent().build();
    }
}