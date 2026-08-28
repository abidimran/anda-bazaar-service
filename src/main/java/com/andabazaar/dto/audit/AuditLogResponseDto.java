package com.andabazaar.dto.audit;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogResponseDto {

    private Long id;

    private Long userId;

    private String action;

    private String entityType;

    private String entityId;

    private String description;

    private String ipAddress;

    private String userAgent;

    private LocalDateTime createdAt;
}