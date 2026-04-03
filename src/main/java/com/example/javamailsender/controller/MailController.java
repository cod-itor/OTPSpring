package com.example.javamailsender.controller;

import com.example.javamailsender.model.dto.OtpRequest;
import com.example.javamailsender.model.dto.OtpResponse;
import com.example.javamailsender.service.OtpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * OTP Controller - REST endpoints for OTP operations
 */
@RestController
@RequestMapping("/api/v1/otp")
@Tag(name = "OTP Management", description = "Endpoints for OTP generation and verification")
public class MailController {
    private static final Logger logger = LoggerFactory.getLogger(MailController.class);

    @Autowired
    private OtpService otpService;

    /**
     * Request OTP for email (GET and POST)
     */
    @PostMapping("/request")
    @Operation(summary = "Request OTP", description = "Generate and send OTP to the provided email")
    public ResponseEntity<OtpResponse> requestOtp(@RequestParam String email) {
        logger.info("OTP request endpoint called with email: {}", email);
        OtpRequest request = new OtpRequest(email);
        OtpResponse response = otpService.requestOtp(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Verify OTP code
     */
    @PostMapping("/verify")
    @Operation(summary = "Verify OTP", description = "Verify if the provided OTP code is correct")
    public ResponseEntity<OtpResponse> verifyOtp(
            @RequestParam String email,
            @RequestParam String otpCode) {
        logger.info("OTP verification for email: {}", email);
        OtpResponse response = otpService.verifyOtp(email, otpCode);
        return ResponseEntity.ok(response);
    }
}

