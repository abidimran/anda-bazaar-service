package com.andabazaar.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.dto.audit.AuditLogRequestDto;
import com.andabazaar.dto.audit.AuditLogResponseDto;
import com.andabazaar.entity.AuditLog;
import com.andabazaar.entity.User;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.AuditLogRepository;
import com.andabazaar.repository.UserRepository;
import com.andabazaar.service.AuditLogService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    @Override
    public AuditLogResponseDto createLog( AuditLogRequestDto request) {

        User user = null;

        if (request.getUserId() != null) {

            user = userRepository.findById( request.getUserId()
            ).orElseThrow(() ->
                    new ResourceNotFoundException(
                            "User not found with id: "
                                    + request.getUserId()
                    ));
        }

        AuditLog log = AuditLog.builder()
                .user(user)
                .action( request.getAction()
                                .trim()
                                .toUpperCase()
                )
                .entityType( request.getEntityType()
                )
                .entityId( request.getEntityId()
                )
                .description( request.getDescription()
                )
                .ipAddress( request.getIpAddress()
                )
                .userAgent( request.getUserAgent()
                )
                .build();

        return mapToResponse(
                auditLogRepository.save(log));
    }

    @Override
    @Transactional(readOnly = true)
    public AuditLogResponseDto getLogById(Long id) {

        return mapToResponse(
                findLog(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponseDto> getAllLogs() {

        return auditLogRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponseDto> getUserLogs( Long userId) {

        if (!userRepository.existsById(userId)) {

            throw new ResourceNotFoundException(
                    "User not found with id: "
                            + userId);
        }

        return auditLogRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponseDto> getLogsByAction( String action) {

        return auditLogRepository
                .findByActionOrderByCreatedAtDesc( action.trim().toUpperCase()
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponseDto> getEntityLogs( String entityType, String entityId) {

        return auditLogRepository
                .findByEntityTypeAndEntityIdOrderByCreatedAtDesc( entityType, entityId )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponseDto> getLogsBetween( LocalDateTime startDate, LocalDateTime endDate) {

        return auditLogRepository
                .findByCreatedAtBetweenOrderByCreatedAtDesc( startDate, endDate )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteLog(Long id) {

        AuditLog log = findLog(id);

        auditLogRepository.delete(log);
    }

    private AuditLog findLog(Long id) {

        return auditLogRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Audit log not found with id: "
                                        + id
                        ));
    }

    private AuditLogResponseDto mapToResponse( AuditLog log) {

        return AuditLogResponseDto.builder()
                .id(log.getId())
                .userId( log.getUser() != null ? log.getUser().getId()
                                : null
                )
                .action(log.getAction())
                .entityType(log.getEntityType())
                .entityId(log.getEntityId())
                .description(log.getDescription())
                .ipAddress(log.getIpAddress())
                .userAgent(log.getUserAgent())
                .createdAt(log.getCreatedAt())
                .build();
    }
}