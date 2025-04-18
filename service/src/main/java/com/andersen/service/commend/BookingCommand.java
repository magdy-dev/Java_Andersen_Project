package com.andersen.service.commend;

import com.andersen.domain.exception.DataAccessException;
import com.andersen.service.exception.BookingServiceException;

/**
 * An interface representing a command for booking operations.
 *
 * @param <T> the type of the result returned by the command execution.
 */
public interface BookingCommand<T> {

    /**
     * Executes the booking command.
     *
     * @return an instance of type T representing the result of the command execution.
     * @throws BookingServiceException if an error occurs during the booking process.
     * @throws DataAccessException if an error occurs while accessing data.
     */
    T execute() throws BookingServiceException, DataAccessException;
}