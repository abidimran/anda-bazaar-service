package com.andabazaar.entity;

import java.time.LocalDateTime;

import com.andabazaar.enums.TicketStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "support_tickets",
    indexes = {
        @Index(
            name = "idx_support_ticket_user",
            columnList = "user_id"
        ),
        @Index(
            name = "idx_support_ticket_status",
            columnList = "status"
        ),
        @Index(
            name = "idx_support_ticket_created",
            columnList = "created_at"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        nullable = false,
        unique = true,
        length = 30
    )
    private String ticketNumber;

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
        length = 200
    )
    private String subject;

    @Column(
        nullable = false,
        length = 2000
    )
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(
        nullable = false,
        length = 30
    )
    @Builder.Default
    private TicketStatus status = TicketStatus.OPEN;

    @Column(length = 20)
    private String priority;

    @Column(length = 100)
    private String category;

    @Column(length = 100)
    private String assignedTo;

    @Column(length = 2000)
    private String resolution;

    private LocalDateTime resolvedAt;

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

        if (ticketNumber == null ||
                ticketNumber.isBlank()) {

            ticketNumber =
                    "TKT-" +
                    System.currentTimeMillis();
        }
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}