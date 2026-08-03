package com.example.orderservice.event;

import java.util.Map;

/**
 * Interface for events that can be audited.
 */
public interface Auditable {

    /**
     * @return The name of the event.
     */
    String getEventName();

    /**
     * @return A map of details about the event.
     */
    Map<String, Object> getEventDetails();
}
