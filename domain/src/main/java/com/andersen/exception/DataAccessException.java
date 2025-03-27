package com.andersen.exception;

public class DataAccessException extends Exception{
    public DataAccessException(String message, ErrorCode workspaceCreationFailed) {
        super(message);
    }
}
