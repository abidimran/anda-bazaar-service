package com.andabazaar.repository.entity;

import com.andabazaar.enums.NotificationType;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.*;

@Entity
@Table(
    name = "notifications",
    indexes = {
        @Index(
            name = "idx_notification_user",
            columnList = "userId"),
        @Index(
            name = "idx_notification_created",
            columnList = "createdAt")
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
        name = "userId",
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
    @Column(name = "isRead", nullable = false)
    @Builder.Default
    private Boolean read = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean sent = false;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime readAt;

    private LocalDateTime sentAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
