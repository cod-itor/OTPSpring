package com.example.javamailsender.service;

import com.example.javamailsender.model.dto.RegisterRequest;
import com.example.javamailsender.model.dto.RegisterResponse;
import com.example.javamailsender.model.dto.LoginRequest;
import com.example.javamailsender.model.dto.LoginResponse;

/**
 * Auth Service Interface - user registration and login
 */
public interface AuthService {
    /**
     * Register a new user and send OTP to email
     */
    RegisterResponse register(RegisterRequest request);
    
    /**
     * Login user (only if verified)
     */
    LoginResponse login(LoginRequest request);
}
