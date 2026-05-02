package com.events.core.command;

/**
 * Marker interface for all commands in the system.
 * Every command must have a unique name used by the HandlerRegistry
 * to find and dispatch the correct handler.
 */
public interface Command {

    /**
     * Returns the unique name of the command.
     * Used as the key in the HandlerRegistry.
     * Convention: use the class simple name, e.g. "CreateOrderCommand"
     */
    String getCommandName();
}
