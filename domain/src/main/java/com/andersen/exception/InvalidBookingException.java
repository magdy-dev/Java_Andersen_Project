package com.andersen.exception;

public class InvalidBookingException extends Exception{
    public InvalidBookingException(String message) {
        super(message);
    }
}
