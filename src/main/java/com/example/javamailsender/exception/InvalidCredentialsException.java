package com.example.javamailsender.exception;

/**
 * Thrown when invalid credentials are provided
 */
public class InvalidCredentialsException extends OtpSenderException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
