package com.andabazaar.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.andabazaar.dto.audit.AuditLogRequestDto;
import com.andabazaar.dto.audit.AuditLogResponseDto;
import com.andabazaar.service.AuditLogService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @PostMapping
    public ResponseEntity<AuditLogResponseDto> createLog(
            @Valid @RequestBody
            AuditLogRequestDto request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        auditLogService.createLog(request)
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLogResponseDto> getLog(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                auditLogService.getLogById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<AuditLogResponseDto>>
            getAllLogs() {

        return ResponseEntity.ok(
                auditLogService.getAllLogs()
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AuditLogResponseDto>>
            getUserLogs(
                    @PathVariable Long userId) {

        return ResponseEntity.ok(
                auditLogService.getUserLogs(userId)
        );
    }

    @GetMapping("/action/{action}")
    public ResponseEntity<List<AuditLogResponseDto>>
            getLogsByAction(
                    @PathVariable String action) {

        return ResponseEntity.ok(
                auditLogService.getLogsByAction(action)
        );
    }

    @GetMapping("/entity")
    public ResponseEntity<List<AuditLogResponseDto>>
            getEntityLogs(
                    @RequestParam String entityType,
                    @RequestParam String entityId) {

        return ResponseEntity.ok(
                auditLogService.getEntityLogs(
                        entityType,
                        entityId
                )
        );
    }

    @GetMapping("/between")
    public ResponseEntity<List<AuditLogResponseDto>>
            getLogsBetween(
                    @RequestParam LocalDateTime startDate,
                    @RequestParam LocalDateTime endDate) {

        return ResponseEntity.ok(
                auditLogService.getLogsBetween(
                        startDate,
                        endDate
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(
            @PathVariable Long id) {

        auditLogService.deleteLog(id);

        return ResponseEntity.noContent().build();
    }
}