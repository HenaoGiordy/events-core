package com.events.core.command;

import java.util.Map;

/**
 * Represents the raw message structure that arrives from the message broker
 * (Kafka, SQS, RabbitMQ, etc.) on the commands topic/queue.
 *
 * The transport layer deserializes the raw JSON into this record
 * and passes it to the HandlerRegistry.
 *
 * Example JSON:
 * {
 *   "commandName": "CreateOrderCommand",
 *   "aggregateId": "order-123",
 *   "payload": {
 *     "customerId": "customer-456",
 *     "amount": 99.99
 *   }
 * }
 */
public record CommandMessage(
        String commandName,
        String aggregateId,
        Map<String, Object> payload
) {}
