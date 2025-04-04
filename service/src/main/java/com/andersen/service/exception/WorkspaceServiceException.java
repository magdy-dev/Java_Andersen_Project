package com.andersen.service.exception;

import com.andersen.service.exception.errorcode.ErrorCode;

/**
 * Custom exception class to handle errors related to the workspace service.
 * This exception is thrown when there are issues during workspace operations,
 * such as validation errors, service failures, or other exceptional conditions.
 */
public class WorkspaceServiceException extends Exception {

    private final ErrorCode errorCode; // Field to store the error code

    /**
     * Constructs a new WorkspaceServiceException with the specified detail message.
     *
     * @param message the detail message, which is saved for later retrieval by the
     *                {@link Throwable#getMessage()} method
     */
    public WorkspaceServiceException(String message) {
        super(message);
        this.errorCode = null; // No error code for this constructor
    }

    /**
     * Constructs a new WorkspaceServiceException with the specified detail message
     * and cause.
     *
     * @param message the detail message, which is saved for later retrieval by the
     *                {@link Throwable#getMessage()} method
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link Throwable#getCause()} method)
     */
    public WorkspaceServiceException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null; // No error code for this constructor
    }

    /**
     * Constructs a new WorkspaceServiceException with the specified detail message
     * and error code.
     *
     * @param message   the detail message
     * @param errorCode the error code for the exception
     */
    public WorkspaceServiceException(String message, ErrorCode errorCode) {
        super(message); // Call to the superclass constructor with the message
        this.errorCode = errorCode; // Store the error code
    }

    /**
     * Returns the error code associated with this exception.
     *
     * @return the error code
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}