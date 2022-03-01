package com.julianhusson.okastock.exception;

public class InvalidRegexException extends RuntimeException{
    public InvalidRegexException(String message) {
        super(message);
    }
}