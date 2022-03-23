package com.julianhusson.okastock.exception;

public class BadUserException extends RuntimeException {
    public BadUserException(String message) {
        super(message);
    }
}
