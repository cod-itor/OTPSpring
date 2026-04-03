package com.example.javamailsender.exception;

/**
 * Thrown when invalid email format is provided
 */
public class InvalidEmailException extends OtpSenderException {
    public InvalidEmailException(String message) {
        super(message);
    }
}
