package com.andersen.service.exception;

/**
 * Custom exception class to handle errors related to the workspace service.
 * This exception is thrown when there are issues during workspace operations,
 * such as validation errors, service failures, or other exceptional conditions.
 */
public class WorkspaceServiceException extends Exception {

    /**
     * Constructs a new WorkspaceServiceException with the specified detail message.
     *
     * @param message the detail message, which is saved for later retrieval by the
     *                {@link Throwable#getMessage()} method
     */
    public WorkspaceServiceException(String message) {
        super(message);
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
    }
}