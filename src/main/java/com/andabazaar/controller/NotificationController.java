package com.andabazaar.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.notification.NotificationRequestDto;
import com.andabazaar.dto.notification.NotificationResponseDto;
import com.andabazaar.service.NotificationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponseDto>
            createNotification(@Valid @RequestBody NotificationRequestDto request) {

 return ResponseEntity.ok(notificationService.createNotification(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<
            List<NotificationResponseDto>>
            getUserNotifications(@PathVariable Long userId) {

 return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<
            List<NotificationResponseDto>>
            getUnreadNotifications(@PathVariable Long userId) {

 return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long>
            getUnreadCount(@PathVariable Long userId) {

 return ResponseEntity.ok(notificationService.getUnreadCount(userId));
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Void>
            markAsRead(@PathVariable Long notificationId, @RequestParam Long userId) {

        notificationService.markAsRead( notificationId, userId);

 return ResponseEntity.ok().build();
    }

    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<Void>
            markAllAsRead(@PathVariable Long userId) {

        notificationService
                .markAllAsRead(userId);

 return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void>
            deleteNotification(@PathVariable Long notificationId, @RequestParam Long userId) {

        notificationService.deleteNotification( notificationId, userId);

 return ResponseEntity.noContent().build();
    }
}