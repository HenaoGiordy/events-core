package com.events.core.registry;

import com.events.core.command.Command;
import com.events.core.command.CommandHandler;
import com.events.core.command.CommandMessage;
import com.events.core.event.Event;
import com.events.core.event.EventHandler;
import com.events.core.event.EventMessage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class HandlerRegistry {

    private final Map<String, CommandHandler<? extends Command>> commandHandlers =
            new ConcurrentHashMap<>();

    private final Map<String, List<EventHandler<? extends Event>>> eventHandlers =
            new ConcurrentHashMap<>();

    public <C extends Command> void registerCommand(CommandHandler<C> handler) {
        String key = handler.getCommandType().getSimpleName();
        if (commandHandlers.putIfAbsent(key, handler) != null) {
            throw new IllegalStateException(
                "A handler is already registered for command: " + key
            );
        }
    }

    public <E extends Event> void registerEvent(EventHandler<E> handler) {
        String key = handler.getEventType().getSimpleName();
        eventHandlers
            .computeIfAbsent(key, k -> new CopyOnWriteArrayList<>())
            .add(handler);
    }


    @SuppressWarnings("unchecked")
    public void dispatchCommand(String commandName, CommandMessage message) {
        CommandHandler<Command> handler =
            (CommandHandler<Command>) commandHandlers.get(commandName);

        if (handler == null) {
            throw new IllegalArgumentException(
                "No handler registered for command: " + commandName
            );
        }

        handler.handle(CommandMapper.map(message, handler.getCommandType()));
    }

    @SuppressWarnings("unchecked")
    public void dispatchEvent(String eventName, EventMessage message) {
        List<EventHandler<? extends Event>> handlers =
            eventHandlers.getOrDefault(eventName, List.of());

        Event event = EventMapper.map(message);
        handlers.forEach(h -> ((EventHandler<Event>) h).handle(event));
    }

    public boolean hasCommandHandler(String commandName) {
        return commandHandlers.containsKey(commandName);
    }

    public boolean hasEventHandler(String eventName) {
        return eventHandlers.containsKey(eventName) && !eventHandlers.get(eventName).isEmpty();
    }

    public int eventHandlerCount(String eventName) {
        return eventHandlers.getOrDefault(eventName, List.of()).size();
    }
}
