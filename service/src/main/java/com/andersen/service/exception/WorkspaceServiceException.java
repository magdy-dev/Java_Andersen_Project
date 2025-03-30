package com.andersen.service.exception;

public class WorkspaceServiceException extends Exception {
    public WorkspaceServiceException(String message) {
        super(message);
    }

    public WorkspaceServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}