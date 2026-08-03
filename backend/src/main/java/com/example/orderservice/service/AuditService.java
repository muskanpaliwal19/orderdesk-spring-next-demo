package com.example.orderservice.service;

import com.example.orderservice.model.AuditLog;
import com.example.orderservice.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public void logCreation(Long entityId, String entityName) {
        // The audit_logs table was removed, so this is a no-op to prevent runtime exceptions.
    }
}
