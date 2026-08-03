package com.example.orderservice.service;

import com.example.orderservice.model.AuditLog;
import com.example.orderservice.model.Order;
import com.example.orderservice.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public void logOrderCreation(Order order) {
        AuditLog auditLog = new AuditLog();
        auditLog.setEntityId(order.getId());
        auditLog.setEntityName("order");
        auditLog.setAction("created");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            auditLog.setChangedBy(authentication.getName());
        }

        auditLogRepository.save(auditLog);
    }
}
