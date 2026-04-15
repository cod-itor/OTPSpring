package com.example.javamailsender.model.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserResponse {
    private Long userId;
    private String fullName;
    private String email;
    private String roleName;
}
