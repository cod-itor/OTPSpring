package com.example.javamailsender.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OTP Request DTO - client sends email to request OTP
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpRequest {
    private String email;
}
