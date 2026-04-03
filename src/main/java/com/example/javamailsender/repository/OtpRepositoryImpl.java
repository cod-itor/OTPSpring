package com.example.javamailsender.repository;

import com.example.javamailsender.model.entity.OtpEntity;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory OTP Repository implementation
 * (Can be replaced with JPA/database implementation later)
 */
@Repository
public class OtpRepositoryImpl implements OtpRepository {
    private final Map<String, OtpEntity> otpStore = new HashMap<>();
    private Long idCounter = 1L;

    @Override
    public OtpEntity save(OtpEntity otp) {
        if (otp.getId() == null) {
            otp.setId(idCounter++);
        }
        otpStore.put(otp.getEmail(), otp);
        return otp;
    }

    @Override
    public Optional<OtpEntity> findByEmail(String email) {
        return Optional.ofNullable(otpStore.get(email));
    }

    @Override
    public void deleteByEmail(String email) {
        otpStore.remove(email);
    }
}
