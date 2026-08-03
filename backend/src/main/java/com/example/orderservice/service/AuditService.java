package com.example.orderservice.service;

import com.example.orderservice.event.Auditable;
import com.example.orderservice.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    private static final Logger logger = LoggerFactory.getLogger(AuditService.class);

    @EventListener
    public void onOrderCreated(Auditable event) {
        if (event instanceof OrderCreatedEvent) {
            logger.info("Audit event: Order created with ID {}", ((OrderCreatedEvent) event).getOrderId());
        } else {
            logger.info("Audit event: {}", event.getEventDetails());
        }
    }
}
