package com.julianhusson.okastock.exception;

public class MailSenderException extends RuntimeException {
    public MailSenderException(String message) {
        super(message);
    }
}
