package com.example.soba2clean.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ResetPasswordDto {
    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@#$%^&+=!*]).{8,}$",
            message = "Password must be at least 8 characters and contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    private String newPassword;

    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@#$%^&+=!*]).{8,}$",
            message = "Confirm Password must be at least 8 characters and contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    @NotBlank(message = "Confirm Password is required")
    private String confirmNewPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }
}
