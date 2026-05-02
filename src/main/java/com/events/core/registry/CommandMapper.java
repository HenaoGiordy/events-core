package com.events.core.registry;

import com.events.core.command.Command;
import com.events.core.command.CommandMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Converts a raw CommandMessage (from the broker) into a concrete Command object.
 *
 * Uses Jackson to map the payload fields to the target command class.
 * The target class is provided by the CommandHandler via getCommandType().
 */
class CommandMapper {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private CommandMapper() {}

    /**
     * Maps a CommandMessage to a concrete Command instance.
     *
     * @param message     the raw message from the broker
     * @param commandType the target command class
     * @param <C>         the command type
     * @return the populated command instance
     */
    static <C extends Command> C map(CommandMessage message, Class<C> commandType) {
        try {
            return MAPPER.convertValue(message.payload(), commandType);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Failed to map CommandMessage to " + commandType.getSimpleName() +
                ". Payload: " + message.payload(), e
            );
        }
    }
}
