package com.andersen.service.exception;

/**
 * Custom exception class to handle authentication errors.
 * This exception is thrown when there are issues related to authentication,
 * such as invalid username or password.
 */
public class AuthenticationException extends Throwable {

    /**
     * Constructs a new AuthenticationException with the specified detail message.
     *
     * @param message the detail message, which is saved for later retrieval by the
     *                {@link Throwable#getMessage()} method
     */
    public AuthenticationException(String message) {
        super(message);
    }
}