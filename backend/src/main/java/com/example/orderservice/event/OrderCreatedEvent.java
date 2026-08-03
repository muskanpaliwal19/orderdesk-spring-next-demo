package com.example.orderservice.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Collections;
import java.util.Map;

@Getter
public class OrderCreatedEvent extends ApplicationEvent implements Auditable {

    private final Long orderId;

    public OrderCreatedEvent(Object source, Long orderId) {
        super(source);
        this.orderId = orderId;
    }

    @Override
    public String getEventName() {
        return "OrderCreated";
    }

    @Override
    public Map<String, Object> getEventDetails() {
        return Collections.singletonMap("orderId", orderId);
    }
}
