package com.andabazaar.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "support_messages",
    indexes = {
        @Index(
            name = "idx_support_message_ticket",
            columnList = "ticket_id"),
        @Index(
            name = "idx_support_message_user",
            columnList = "user_id"),
        @Index(
            name = "idx_support_message_created",
            columnList = "created_at")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "ticket_id",
        nullable = false
    )
    private SupportTicket ticket;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "user_id",
        nullable = false
    )
    private User user;

    @Column(
        nullable = false,
        length = 3000
    )
    private String message;

    @Column(
        nullable = false
    )
    @Builder.Default
    private Boolean adminReply = false;

    @Column(
        nullable = false,
        updatable = false
    )
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}