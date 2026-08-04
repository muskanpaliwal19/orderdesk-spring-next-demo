package com.example.auditlog.service;

import com.example.auditlog.AuditEventType;
import com.example.auditlog.jpa.AuditLog;
import com.example.auditlog.repository.AuditLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(AuditLogService.class);

    @Autowired
    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public List<AuditLog> getRecentAuditLogs() {
        return auditLogRepository.findTop50ByOrderByCreatedAtDesc();
    }

    public void logEvent(AuditEventType eventType, Map<String, Object> eventDetailsMap) {
        AuditLog auditLog = new AuditLog();
        auditLog.setEventType(eventType.name());
        try {
            auditLog.setMessage(objectMapper.writeValueAsString(eventDetailsMap));
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize audit event details for event type {}", eventType.name(), e);
            auditLog.setMessage("{\"error\":\"Failed to serialize event details\"}");
        }
        auditLog.setCreatedAt(LocalDateTime.now());
        auditLogRepository.save(auditLog);
    }
}
