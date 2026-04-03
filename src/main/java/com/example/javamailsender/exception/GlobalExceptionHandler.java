package com.example.javamailsender.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler - centralized error handling
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<Map<String, Object>> handleEmailSendingException(EmailSendingException e) {
        logger.error("Email sending failed: {}", e.getMessage(), e);
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Email Sending Failed",
                e.getMessage()
        );
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidEmailException(InvalidEmailException e) {
        logger.warn("Invalid email: {}", e.getMessage());
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid Email",
                e.getMessage()
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        logger.warn("User already exists: {}", e.getMessage());
        return buildErrorResponse(
                HttpStatus.CONFLICT,
                "User Already Exists",
                e.getMessage()
        );
    }

    @ExceptionHandler(UserNotVerifiedException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotVerifiedException(UserNotVerifiedException e) {
        logger.warn("User not verified: {}", e.getMessage());
        return buildErrorResponse(
                HttpStatus.FORBIDDEN,
                "User Not Verified",
                e.getMessage()
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentialsException(InvalidCredentialsException e) {
        logger.warn("Invalid credentials: {}", e.getMessage());
        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Invalid Credentials",
                e.getMessage()
        );
    }

    @ExceptionHandler(OtpSenderException.class)
    public ResponseEntity<Map<String, Object>> handleOtpSenderException(OtpSenderException e) {
        logger.error("OTP Sender error: {}", e.getMessage(), e);
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "OTP Service Error",
                e.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {
        logger.error("Unexpected error: {}", e.getMessage(), e);
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "An unexpected error occurred. Please try again later."
        );
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String error, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", error);
        response.put("message", message);
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, status);
    }
}
