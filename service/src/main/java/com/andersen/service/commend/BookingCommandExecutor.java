package com.andersen.service.commend;

import com.andersen.service.exception.BookingServiceException;
import org.springframework.stereotype.Component;

/**
 * A component that executes booking commands.
 */
@Component
public class BookingCommandExecutor {

    /**
     * Executes a given booking command.
     *
     * @param command the booking command to execute.
     * @param <T>     the type of the result returned by the command execution.
     * @return an instance of type T representing the result of the command execution.
     * @throws com.andersen.domain.exception.DataAccessException if an error occurs while accessing data.
     * @throws RuntimeException if the booking command execution fails due to a BookingServiceException.
     */
    public <T> T execute(BookingCommand<T> command) throws com.andersen.domain.exception.DataAccessException {
        try {
            return command.execute();
        } catch (BookingServiceException e) {
            throw new RuntimeException("Command execution failed: " + e.getMessage(), e);
        }
    }
}