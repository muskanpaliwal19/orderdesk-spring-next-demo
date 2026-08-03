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
        AuditLog auditLog = new AuditLog();
        auditLog.setEntityId(entityId);
        auditLog.setEntityName(entityName);
        auditLog.setAction("created");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            auditLog.setChangedBy(authentication.getName());
        } else {
            auditLog.setChangedBy("system");
        }

        auditLogRepository.save(auditLog);
    }
}
