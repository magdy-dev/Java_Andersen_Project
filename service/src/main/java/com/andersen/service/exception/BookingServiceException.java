package com.andersen.service.exception;

import com.andersen.service.exception.errorcode.ErrorCode;

/**
 * Custom exception class to handle errors related to the booking service.
 * This exception is thrown when there are issues during the booking process,
 * such as validation errors or service failures.
 */
public class BookingServiceException extends Exception {

    private final ErrorCode errorCode; // Field to store the error code

    public BookingServiceException(String message, ErrorCode errorCode) {
        super(message); // Call parent constructor with the message
        this.errorCode = errorCode; // Store the error code
    }

    // Getter for the error code
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}