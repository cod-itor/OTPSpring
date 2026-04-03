package com.example.javamailsender.service;

import com.example.javamailsender.model.dto.OtpRequest;
import com.example.javamailsender.model.dto.OtpResponse;

/**
 * OTP Service Interface - high-level OTP operations
 */
public interface OtpService {
    /**
     * Request OTP for email and send via email
     */
    OtpResponse requestOtp(OtpRequest request);
    
    /**
     * Verify OTP code
     */
    OtpResponse verifyOtp(String email, String otpCode);
}
