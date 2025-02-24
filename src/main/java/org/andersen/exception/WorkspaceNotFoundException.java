package org.andersen.exception;

public class WorkspaceNotFoundException extends Exception {
    public WorkspaceNotFoundException(String message) {
        super(message);
    }


    public WorkspaceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
