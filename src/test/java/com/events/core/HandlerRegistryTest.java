package com.events.core;

import com.events.core.command.Command;
import com.events.core.command.CommandHandler;
import com.events.core.command.CommandMessage;
import com.events.core.event.Event;
import com.events.core.event.EventHandler;
import com.events.core.event.EventMessage;
import com.events.core.registry.HandlerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class HandlerRegistryTest {

    private HandlerRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new HandlerRegistry();
    }

    // --- Commands ---

    @Test
    void shouldDispatchCommandToRegisteredHandler() {
        AtomicBoolean handled = new AtomicBoolean(false);

        registry.registerCommand(new CommandHandler<TestCommand>() {
            @Override public void handle(TestCommand command) { handled.set(true); }
            @Override public Class<TestCommand> getCommandType() { return TestCommand.class; }
        });

        registry.dispatchCommand("TestCommand",
            new CommandMessage("TestCommand", "agg-1", Map.of()));

        assertTrue(handled.get());
    }

    @Test
    void shouldThrowWhenNoCommandHandlerRegistered() {
        assertThrows(IllegalArgumentException.class, () ->
            registry.dispatchCommand("UnknownCommand",
                new CommandMessage("UnknownCommand", "agg-1", Map.of()))
        );
    }

    @Test
    void shouldThrowWhenDuplicateCommandHandlerRegistered() {
        CommandHandler<TestCommand> handler = new CommandHandler<>() {
            @Override public void handle(TestCommand command) {}
            @Override public Class<TestCommand> getCommandType() { return TestCommand.class; }
        };

        registry.registerCommand(handler);
        assertThrows(IllegalStateException.class, () -> registry.registerCommand(handler));
    }

    // --- Events ---

    @Test
    void shouldDispatchEventToAllRegisteredHandlers() {
        AtomicInteger count = new AtomicInteger(0);

        EventHandler<Event> h1 = new EventHandler<>() {
            @Override public void handle(Event event) { count.incrementAndGet(); }
            @Override public Class<Event> getEventType() { return Event.class; }
        };
        EventHandler<Event> h2 = new EventHandler<>() {
            @Override public void handle(Event event) { count.incrementAndGet(); }
            @Override public Class<Event> getEventType() { return Event.class; }
        };

        registry.registerEvent(h1);
        registry.registerEvent(h2);

        registry.dispatchEvent("Event",
            new EventMessage("Event", "agg-1", Instant.now(), Map.of()));

        assertEquals(2, count.get());
    }

    @Test
    void shouldSilentlyIgnoreEventWithNoHandlers() {
        assertDoesNotThrow(() ->
            registry.dispatchEvent("NoHandlerEvent",
                new EventMessage("NoHandlerEvent", "agg-1", Instant.now(), Map.of()))
        );
    }

    // --- Helpers ---

    static class TestCommand implements Command {
        @Override public String getCommandName() { return "TestCommand"; }
    }
}
