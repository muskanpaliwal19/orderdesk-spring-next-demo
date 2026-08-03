package com.example.orderservice.service;

import com.example.orderservice.event.Auditable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    private static final Logger logger = LoggerFactory.getLogger(AuditService.class);

    @EventListener
    public void onAuditableEvent(Auditable event) {
        logger.info("Audit event: name={}, details={}", event.getEventName(), event.getEventDetails());
    }
}
