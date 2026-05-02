package com.events.core.event;

import java.time.Instant;
import java.util.Map;

public interface Event {

    String getEventName();

    String getAggregateId();

    Instant getOccurredAt();

    Map<String, Object> toPayload();
}
