package com.example.javamailsender.exception;

/**
 * Thrown when email sending fails
 */
public class EmailSendingException extends OtpSenderException {
    public EmailSendingException(String message) {
        super(message);
    }

    public EmailSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
