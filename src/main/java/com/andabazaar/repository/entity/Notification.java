package com.andabazaar.repository.entity;

import java.time.LocalDateTime;

import com.andabazaar.enums.NotificationType;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "notifications",
    indexes = {
        @Index(
            name = "idx_notification_user",
            columnList = "user_id"),
        @Index(
            name = "idx_notification_created",
            columnList = "created_at")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false
    )
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(
        nullable = false,
        length = 40
    )
    private NotificationType type;

    @Column(
        nullable = false,
        length = 200
    )
    private String title;

    @Column(
        nullable = false,
        columnDefinition = "TEXT")
    private String message;

    // CHANGED
    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private Boolean read = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean sent = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime readAt;

    private LocalDateTime sentAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}