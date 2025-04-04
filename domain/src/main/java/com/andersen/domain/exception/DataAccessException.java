package com.andersen.domain.exception;

import com.andersen.domain.exception.errorCode.ErrorCodeRepo;

/**
 * Custom exception class to handle errors related to data access operations.
 * This exception is thrown when there are issues accessing the data layer,
 * such as database connection errors or transaction failures.
 */
public class DataAccessException extends Exception {

    private final ErrorCodeRepo errorCode;

    /**
     * Constructs a new DataAccessException with a specified message and error code.
     *
     * @param message the detail message for this exception
     * @param errorCode the error code associated with this exception
     */
    public DataAccessException(String message, ErrorCodeRepo errorCode) {
        super(message); // Call the constructor of the superclass
        this.errorCode = errorCode; // Store the error code
    }

    /**
     * Retrieves the error code associated with this exception.
     *
     * @return the error code
     */
    public ErrorCodeRepo getErrorCode() {
        return errorCode;
    }
}