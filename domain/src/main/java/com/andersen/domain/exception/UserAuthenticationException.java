package com.andersen.domain.exception;

/**
 * Custom exception class to handle errors related to user authentication.
 * This exception is thrown when authentication attempts fail due to issues
 * such as invalid credentials or locked accounts.
 */
public class UserAuthenticationException extends Exception {

    /**
     * Constructs a new UserAuthenticationException with the specified detail message.
     *
     * @param message the detail message, which is saved for later retrieval by the
     *                {@link Throwable#getMessage()} method
     */
    public UserAuthenticationException(String message) {
        super(message);
    }

}