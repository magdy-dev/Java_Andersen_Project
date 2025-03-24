package com.andersen.service.excption;

public class AuthenticationException extends Throwable {
    public AuthenticationException(String invalidUsernameOrPassword) {
        super(invalidUsernameOrPassword);
    }
}
