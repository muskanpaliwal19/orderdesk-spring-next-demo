package com.example.orderservice.event;

import java.util.Map;

/**
 * Interface for events that can be audited.
 */
public interface Auditable {

    /**
     * @return A map of details about the event.
     */
    String getEventName();
    Map<String, Object> getEventDetails();
}
