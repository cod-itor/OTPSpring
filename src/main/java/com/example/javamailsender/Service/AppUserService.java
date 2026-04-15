package com.example.javamailsender.Service;

import com.example.javamailsender.model.Request.AppUserRequest;
import com.example.javamailsender.model.Response.AppUserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppUserService extends UserDetailsService {
    AppUserResponse register(AppUserRequest request);
}
