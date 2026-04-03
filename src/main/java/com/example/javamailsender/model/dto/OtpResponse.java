package com.example.javamailsender.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OTP Response DTO - server responds with status
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpResponse {
    private boolean success;
    private String message;
    private String email;
    
    public OtpResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
