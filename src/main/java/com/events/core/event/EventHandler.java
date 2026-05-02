package com.events.core.event;


public interface EventHandler<E extends Event> {

    void handle(E event);

    Class<E> getEventType();
}
