package com.example.javamailsender.repository;

import com.example.javamailsender.model.entity.OtpEntity;
import java.util.Optional;

/**
 * OTP Repository - stores and retrieves OTP records
 */
public interface OtpRepository {
    OtpEntity save(OtpEntity otp);
    Optional<OtpEntity> findByEmail(String email);
    void deleteByEmail(String email);
}
