package com.example.javamailsender.exception;

/**
 * Base custom exception
 */
public class OtpSenderException extends RuntimeException {
    public OtpSenderException(String message) {
        super(message);
    }

    public OtpSenderException(String message, Throwable cause) {
        super(message, cause);
    }
}
