package com.example.javamailsender.exception;

/**
 * Thrown when user already exists
 */
public class UserAlreadyExistsException extends OtpSenderException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
