package com.example.auditlog.controller.dto;

import com.example.auditlog.jpa.AuditLog;
import java.time.LocalDateTime;

public class AuditLogDto {

    private String eventType;
    private String message;
    private LocalDateTime createdAt;

    public AuditLogDto(String eventType, String message, LocalDateTime createdAt) {
        this.eventType = eventType;
        this.message = message;
        this.createdAt = createdAt;
    }

    public static AuditLogDto fromEntity(AuditLog auditLog) {
        return new AuditLogDto(
            auditLog.getEventType(),
            auditLog.getMessage(),
            auditLog.getCreatedAt()
        );
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
