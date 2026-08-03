package com.example.orderservice.service;

import com.example.orderservice.event.OrderCreatedEvent;
import com.example.orderservice.model.AuditLog;
import com.example.orderservice.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    @EventListener
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        AuditLog auditLog = new AuditLog();
        auditLog.setEntityId(event.getOrder().getId());
        auditLog.setEntityName("order");
        auditLog.setAction("created");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            auditLog.setChangedBy(authentication.getName());
        }

        auditLogRepository.save(auditLog);
    }
}
