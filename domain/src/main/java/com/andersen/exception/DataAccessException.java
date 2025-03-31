package com.andersen.exception;

import com.andersen.exception.errorCode.ErrorCode;

/**
 * Custom exception class to handle errors related to data access operations.
 * This exception is thrown when there are issues accessing the data layer,
 * such as database connection errors or transaction failures.
 */
public class DataAccessException extends Exception {

    /**
     * Constructs a new DataAccessException with the specified detail message
     * and error code.
     *
     * @param message the detail message, which is saved for later retrieval by the
     *                {@link Throwable#getMessage()} method
     * @param errorCode the specific error code representing the type of failure
     *                  (e.g., workspace creation failure)
     */
    public DataAccessException(String message, ErrorCode errorCode) {
        super(message);
    }
}