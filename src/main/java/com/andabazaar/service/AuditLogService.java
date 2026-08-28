package com.andabazaar.service;

import java.time.LocalDateTime;
import java.util.List;

import com.andabazaar.dto.audit.AuditLogRequestDto;
import com.andabazaar.dto.audit.AuditLogResponseDto;

public interface AuditLogService {

    AuditLogResponseDto createLog(
            AuditLogRequestDto request);

    AuditLogResponseDto getLogById(
            Long id);

    List<AuditLogResponseDto> getAllLogs();

    List<AuditLogResponseDto> getUserLogs(
            Long userId);

    List<AuditLogResponseDto> getLogsByAction(
            String action);

    List<AuditLogResponseDto> getEntityLogs(
            String entityType,
            String entityId);

    List<AuditLogResponseDto> getLogsBetween(
            LocalDateTime startDate,
            LocalDateTime endDate);

    void deleteLog(Long id);
}