package com.example.javamailsender.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * User Entity - stores user registration and profile info
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    private Long id;
    private String name;
    private String email;
    private String password;
    private boolean isVerified;  // true after OTP verification
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
