package com.example.javamailsender.model.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserRequest {
    private String fullName;
    private String email;
    private String password;
    private String roleName;
}
