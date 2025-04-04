package com.andersen.service.exception;

import com.andersen.service.exception.errorcode.ErrorCode;

public class DataAccessException extends Exception {
    private final String errorCode;

    public DataAccessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode.getCode();
    }

    public String getErrorCode() {
        return errorCode;
    }
}

