package com.events.core.event;

import java.time.Instant;
import java.util.UUID;

public abstract class BaseEvent implements Event {

    private final String eventId;
    private final String eventName;
    private final String aggregateId;
    private final Instant occurredAt;

    protected BaseEvent(String aggregateId, String eventName) {
        this.eventId     = UUID.randomUUID().toString();
        this.aggregateId = aggregateId;
        this.eventName   = eventName;
        this.occurredAt  = Instant.now();
    }

    public String getEventId()     { return eventId; }

    @Override
    public String getEventName()   { return eventName; }

    @Override
    public String getAggregateId() { return aggregateId; }

    @Override
    public Instant getOccurredAt() { return occurredAt; }
}
