package com.example.javamailsender.controller;

import com.example.javamailsender.Service.AppUserService;
import com.example.javamailsender.exception.InvalidCredentialsException;
import com.example.javamailsender.jwt.JwtService;
import com.example.javamailsender.model.dto.*;
import com.example.javamailsender.model.Response.AuthResponse;
import com.example.javamailsender.service.AuthService;
import com.example.javamailsender.service.OtpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Auth Controller - REST endpoints for user authentication
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "User registration, login, and email verification")
@RequiredArgsConstructor
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final OtpService otpService;
    private final AppUserService appUserService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private void authenticate(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new InvalidCredentialsException("USER_DISABLED");
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("INVALID_CREDENTIALS");
        }
    }

    /**
     * Register a new user
     * Automatically sends OTP to email
     */
    @PostMapping("/register")
    @Operation(summary = "Register User", description = "Register a new user with name, email, and password. OTP will be sent to email automatically.")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        logger.info("Registration request for email: {}", request.getEmail());
        RegisterResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Verify OTP and mark user as verified
     */
    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP", description = "Verify the OTP code sent to user's email. Required before login.")
    public ResponseEntity<OtpResponse> verifyOtp(
            @RequestParam String email,
            @RequestParam String otpCode) {
        logger.info("OTP verification for email: {}", email);
        OtpResponse response = otpService.verifyOtp(email, otpCode);
        return ResponseEntity.ok(response);
    }

    /**
     * Request new OTP (resend OTP)
     */
    @PostMapping("/resend-otp")
    @GetMapping("/resend-otp")
    @Operation(summary = "Resend OTP", description = "Request a new OTP to be sent to user's email")
    public ResponseEntity<OtpResponse> resendOtp(@RequestParam String email) {
        logger.info("Resend OTP request for email: {}", email);
        OtpRequest request = new OtpRequest(email);
        OtpResponse response = otpService.requestOtp(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Login user
     * User must be verified before login
     */
    @PostMapping("/login")
    @Operation(summary = "Login User", description = "Login with email and password. User must be verified with OTP first. Returns JWT token on success.")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        logger.info("Login request for email: {}", request.getEmail());

        // Keep existing OTP verification gate (must be verified=true)
        authService.login(request);

        // Authenticate against Spring Security/AppUser credentials
        authenticate(request.getEmail(), request.getPassword());

        final UserDetails userDetails = appUserService.loadUserByUsername(request.getEmail());
        final String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
