package com.example.orderservice.service;

import com.example.orderservice.event.OrderCreatedEvent;
import com.example.orderservice.model.AuditLog;
import com.example.orderservice.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    @TransactionalEventListener
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        AuditLog auditLog = new AuditLog();
        auditLog.setEntityId(event.getOrder().getId());
        auditLog.setEntityName("order");
        auditLog.setAction("created");
        auditLogRepository.save(auditLog);
    }
}
