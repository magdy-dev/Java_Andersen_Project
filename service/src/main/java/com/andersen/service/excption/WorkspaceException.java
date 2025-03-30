package com.andersen.service.excption;

public class WorkspaceException extends Exception {
    public WorkspaceException(String message) {
        super(message);
    }

    public WorkspaceException(String message, Throwable cause) {
        super(message, cause);
    }
}