package com.example.audit.dto;

import java.time.LocalDateTime;

public record AuditLogDto(
    Long id,
    String eventName,
    String eventDescription,
    String entityName,
    String entityId,
    String userId,
    LocalDateTime timestamp,
    String details
) {}
