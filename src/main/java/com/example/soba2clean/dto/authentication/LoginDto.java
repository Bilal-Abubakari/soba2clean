package com.example.soba2clean.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginDto {
    @Email(message = "Email is required")
    String email;

    @NotBlank(message = "Password is required")
    String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
