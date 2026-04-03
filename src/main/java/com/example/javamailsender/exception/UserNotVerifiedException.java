package com.example.javamailsender.exception;

/**
 * Thrown when user is not verified
 */
public class UserNotVerifiedException extends OtpSenderException {
    public UserNotVerifiedException(String message) {
        super(message);
    }
}
