package com.andersen.service.exception;

public class AuthenticationException extends Throwable {
    public AuthenticationException(String invalidUsernameOrPassword) {
        super(invalidUsernameOrPassword);
    }
}
