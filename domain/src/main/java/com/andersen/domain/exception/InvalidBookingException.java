package com.andersen.domain.exception;

/**
 * Custom exception class to handle errors related to invalid booking operations.
 * This exception is thrown when a booking attempt fails due to invalid parameters
 * or conditions, such as overlapping booking times or invalid booking details.
 */
public class InvalidBookingException extends Exception {

    /**
     * Constructs a new InvalidBookingException with the specified detail message.
     *
     * @param message the detail message, which is saved for later retrieval by the
     *                {@link Throwable#getMessage()} method
     */
    public InvalidBookingException(String message) {
        super(message);
    }
}