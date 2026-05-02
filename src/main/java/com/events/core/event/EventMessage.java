package com.events.core.event;

import java.time.Instant;
import java.util.Map;

public record EventMessage(
        String eventName,
        String aggregateId,
        Instant occurredAt,
        Map<String, Object> payload
) {}
