package com.andersen.service.exception;

/**
 * Custom exception class to handle errors related to the booking service.
 * This exception is thrown when there are issues during the booking process,
 * such as validation errors or service failures.
 */
public class BookingServiceException extends Exception {

    /**
     * Constructs a new BookingServiceException with the specified detail message.
     *
     * @param message the detail message, which is saved for later retrieval by the
     *                {@link Throwable#getMessage()} method
     */
    public BookingServiceException(String message) {
        super(message);
    }
}