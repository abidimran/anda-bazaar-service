package com.andabazaar.controller;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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
import com.andabazaar.dto.common.PagedResponse;
import com.andabazaar.service.AuditLogService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Audit Logs", description = "System audit log tracking")
@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @Operation(summary = "Create Log")
    @PostMapping
    public ResponseEntity<AuditLogResponseDto> createLog(@Valid @RequestBody AuditLogRequestDto request) {

 return ResponseEntity.status(HttpStatus.CREATED).body(auditLogService.createLog(request));
    }

    @Operation(summary = "Get Log")
    @GetMapping("/{id}")
    public ResponseEntity<AuditLogResponseDto> getLog(@PathVariable Long id) {

 return ResponseEntity.ok(auditLogService.getLogById(id));
    }

    @Operation(summary = "Get All Logs")
    @GetMapping
    public ResponseEntity<PagedResponse<AuditLogResponseDto>>
            getAllLogs(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(auditLogService.getAllLogs(), page, size));
    }

    @Operation(summary = "Get User Logs")
    @GetMapping("/user/{userId}")
    public ResponseEntity<PagedResponse<AuditLogResponseDto>>
            getUserLogs(@PathVariable Long userId,
                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(auditLogService.getUserLogs(userId), page, size));
    }

    @Operation(summary = "Get Logs By Action")
    @GetMapping("/action/{action}")
    public ResponseEntity<PagedResponse<AuditLogResponseDto>>
            getLogsByAction(@PathVariable String action,
                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(auditLogService.getLogsByAction(action), page, size));
    }

    @Operation(summary = "Get Entity Logs")
    @GetMapping("/entity")
    public ResponseEntity<PagedResponse<AuditLogResponseDto>>
            getEntityLogs(@RequestParam String entityType, @RequestParam String entityId,
                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(auditLogService.getEntityLogs(entityType, entityId), page, size));
    }

    @Operation(summary = "Get Logs Between")
    @GetMapping("/between")
    public ResponseEntity<PagedResponse<AuditLogResponseDto>>
            getLogsBetween(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate,
                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(auditLogService.getLogsBetween(startDate, endDate), page, size));
    }

    @Operation(summary = "Delete Log")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long id) {

        auditLogService.deleteLog(id);

 return ResponseEntity.noContent().build();
    }
}
