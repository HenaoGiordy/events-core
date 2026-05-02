package com.events.core.registry;

import com.events.core.event.Event;
import com.events.core.event.EventMessage;

import java.time.Instant;
import java.util.Map;

/**
 * Converts a raw EventMessage (from the broker) into an Event object
 * that can be dispatched to registered EventHandlers.
 *
 * Returns a GenericEvent backed by the raw payload map.
 * If your EventHandlers need a strongly-typed event, override this behavior
 * by extending HandlerRegistry and providing your own mapping logic.
 */
class EventMapper {

    private EventMapper() {}

    static Event map(EventMessage message) {
        return new GenericEvent(message.eventName(), message.aggregateId(),
                                message.occurredAt(), message.payload());
    }

    /**
     * Simple Event implementation backed by the raw payload map.
     * Cast the payload values in your EventHandler as needed.
     */
    record GenericEvent(
            String eventName,
            String aggregateId,
            Instant occurredAt,
            Map<String, Object> payload
    ) implements Event {

        @Override public String getEventName()   { return eventName; }
        @Override public String getAggregateId() { return aggregateId; }
        @Override public Instant getOccurredAt() { return occurredAt; }
        @Override public Map<String, Object> toPayload() { return payload; }
    }
}
