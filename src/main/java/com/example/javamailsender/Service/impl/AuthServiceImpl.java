package com.example.javamailsender.service.impl;

import com.example.javamailsender.exception.InvalidCredentialsException;
import com.example.javamailsender.exception.UserAlreadyExistsException;
import com.example.javamailsender.exception.UserNotVerifiedException;
import com.example.javamailsender.model.dto.*;
import com.example.javamailsender.model.entity.OtpEntity;
import com.example.javamailsender.model.entity.UserEntity;
import com.example.javamailsender.repository.OtpRepository;
import com.example.javamailsender.repository.UserRepository;
import com.example.javamailsender.service.AuthService;
import com.example.javamailsender.service.OtpService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Auth Service Implementation - user registration and authentication
 */
@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private OtpRepository otpRepository;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        logger.info("Registration request for email: {}", request.getEmail());

        // Check if user already exists
        Optional<UserEntity> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }

        try {
            // Create new user (not verified yet)
            UserEntity user = new UserEntity();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword()); // TODO: Hash password in production
            user.setVerified(false);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());

            // Save user
            UserEntity savedUser = userRepository.save(user);
            logger.info("User registered: {} with ID: {}", request.getEmail(), savedUser.getId());

            // Send OTP to email automatically
            OtpRequest otpRequest = new OtpRequest(request.getEmail());
            otpService.requestOtp(otpRequest);
            logger.info("OTP sent to: {}", request.getEmail());

            return new RegisterResponse(
                    true,
                    "Registration successful! OTP sent to your email. Please verify to complete registration.",
                    savedUser.getId(),
                    savedUser.getEmail(),
                    savedUser.getName()
            );

        } catch (Exception e) {
            logger.error("Registration failed for {}: {}", request.getEmail(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        logger.info("Login request for email: {}", request.getEmail());

        // Find user by email
        Optional<UserEntity> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty()) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        UserEntity user = userOptional.get();

        // Check if verified
        if (!user.isVerified()) {
            logger.warn("Login attempt by unverified user: {}", request.getEmail());
            throw new UserNotVerifiedException("User not verified. Please verify your email with OTP first.");
        }

        // Verify password (TODO: use bcrypt in production)
        if (!user.getPassword().equals(request.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        logger.info("Login successful for: {}", request.getEmail());

        return new LoginResponse(
                true,
                "Login successful!",
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.isVerified()
        );
    }
}
