package com.example.javamailsender.controller;

import com.example.javamailsender.Service.MailService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;

@RestController
@RequestMapping("/api/v1")
public class MailController {
    private static final Logger logger = LoggerFactory.getLogger(MailController.class);

    @Autowired
    private MailService otpEmailService;

    private final SecureRandom random = new SecureRandom();

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestParam String email) {
        logger.info("Received OTP request for email: {}", email);
        
        // Validate email
        if (email == null || email.trim().isEmpty()) {
            logger.warn("Empty email provided");
            return ResponseEntity.badRequest().body("Email cannot be empty");
        }

        // generate a secure random 6-digit OTP
        int otp = 100000 + random.nextInt(900000);
        String generatedOtp = String.valueOf(otp);
        logger.info("Generated OTP: {} for email: {}", generatedOtp, email);

        try {
            otpEmailService.sendOtpEmail(email, generatedOtp);
            logger.info("OTP email sent successfully to: {}", email);
            return ResponseEntity.ok("OTP Sent Successfully");
        } catch (MessagingException e) {
            logger.error("Messaging exception while sending OTP to {}: {}", email, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending email: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected exception while sending OTP to {}: {}", email, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }
}
