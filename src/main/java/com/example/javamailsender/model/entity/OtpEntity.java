package com.example.javamailsender.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * OTP Entity - stores OTP records
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpEntity {
    private Long id;
    private String email;
    private String otpCode;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean verified;
}
