package com.andersen.service.exception;

/**
 * Custom exception class to handle registration errors.
 * This exception is thrown when there are issues related to user registration,
 * such as validation failures or other errors during the registration process.
 */
public class RegistrationException extends Exception {

    /**
     * Constructs a new RegistrationException with the specified detail message.
     *
     * @param message the detail message, which is saved for later retrieval by the
     *                {@link Throwable#getMessage()} method
     */
    public RegistrationException(String message) {
        super(message);
    }
}