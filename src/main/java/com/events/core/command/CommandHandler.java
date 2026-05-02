package com.events.core.command;

/**
 * Interface that every command handler must implement.
 * The HandlerRegistry uses getCommandType() to register
 * and look up the handler by command name.
 *
 * @param <C> the specific Command type this handler processes
 */
public interface CommandHandler<C extends Command> {

    /**
     * Executes the business logic for the given command.
     *
     * @param command the command to handle
     */
    void handle(C command);

    /**
     * Returns the Class of the command this handler processes.
     * Used by the HandlerRegistry to build the lookup key.
     *
     * @return the command class
     */
    Class<C> getCommandType();
}
