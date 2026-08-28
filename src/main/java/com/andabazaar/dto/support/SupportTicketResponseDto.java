package com.andabazaar.dto.support;

import java.time.LocalDateTime;

import com.andabazaar.enums.TicketStatus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportTicketResponseDto {

    private Long id;

    private String ticketNumber;

    private Long userId;

    private String subject;

    private String description;

    private TicketStatus status;

    private String priority;

    private String category;

    private String assignedTo;

    private String resolution;

    private LocalDateTime resolvedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}