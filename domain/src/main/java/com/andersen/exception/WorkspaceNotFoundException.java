package com.andersen.exception;

/**
 * Custom exception class to handle errors when a workspace cannot be found.
 * This exception is thrown when an operation references a workspace that does
 * not exist, such as trying to retrieve, update, or delete a workspace by its ID.
 */
public class WorkspaceNotFoundException extends Exception {

    /**
     * Constructs a new WorkspaceNotFoundException with the specified detail message.
     *
     * @param message the detail message, which is saved for later retrieval by the
     *                {@link Throwable#getMessage()} method
     */
    public WorkspaceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new WorkspaceNotFoundException with the specified detail message
     * and cause.
     *
     * @param message the detail message, which is saved for later retrieval
     * @param cause the cause of the exception, which can be retrieved later
     *              using {@link Throwable#getCause()} method
     */
    public WorkspaceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}