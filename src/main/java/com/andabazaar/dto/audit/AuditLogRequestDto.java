package com.andabazaar.dto.audit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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
public class AuditLogRequestDto {

    private Long userId;

    @NotBlank(message = "Action is required")
    @Size(max = 100, message = "Action cannot exceed 100 characters")
    private String action;

    @Size(max = 100, message = "Entity type cannot exceed 100 characters")
    private String entityType;

    @Size(max = 100, message = "Entity ID cannot exceed 100 characters")
    private String entityId;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Size(max = 45, message = "IP address cannot exceed 45 characters")
    private String ipAddress;

    @Size(max = 500, message = "User agent cannot exceed 500 characters")
    private String userAgent;
}