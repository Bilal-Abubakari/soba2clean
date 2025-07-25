package com.example.soba2clean.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ForgotPasswordDto {
    @Email(message = "Email is required")
    @NotBlank(message = "Email must be a valid email address")
    private String email;

    public String getEmail() {
        return email;
    }

}
