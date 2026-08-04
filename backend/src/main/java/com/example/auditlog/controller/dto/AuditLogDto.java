package com.example.auditlog.controller.dto;

import com.example.auditlog.jpa.AuditLog;
import java.time.LocalDateTime;

public class AuditLogDto {

    private String eventType;
    private String eventDetails;
    private LocalDateTime createdAt;

    public AuditLogDto(String eventType, String eventDetails, LocalDateTime createdAt) {
        this.eventType = eventType;
        this.eventDetails = eventDetails;
        this.createdAt = createdAt;
    }

    public static AuditLogDto fromEntity(AuditLog auditLog) {
        return new AuditLogDto(
            auditLog.getEventType(),
            auditLog.getEventDetails(),
            auditLog.getCreatedAt()
        );
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
