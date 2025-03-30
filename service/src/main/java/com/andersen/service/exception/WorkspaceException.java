package com.andersen.service.exception;

public class WorkspaceException extends Exception {
    public WorkspaceException(String message) {
        super(message);
    }

    public WorkspaceException(String message, Throwable cause) {
        super(message, cause);
    }
}