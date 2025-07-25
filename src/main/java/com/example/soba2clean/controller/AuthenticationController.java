package com.example.soba2clean.controller;

import com.example.soba2clean.dto.authentication.ForgotPasswordDto;
import com.example.soba2clean.dto.authentication.LoginDto;
import com.example.soba2clean.dto.authentication.RegisterDto;
import com.example.soba2clean.dto.authentication.ResetPasswordDto;
import com.example.soba2clean.model.User;
import com.example.soba2clean.response.ApiResponse;
import com.example.soba2clean.response.authentication.LoginResponse;
import com.example.soba2clean.service.authentication.AuthenticationService;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.UUID;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@Validated
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ApiResponse<User> register(@Valid @RequestBody RegisterDto registerDto) {
        return this.authenticationService.register(registerDto);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginDto loginDto) {
        return authenticationService.login(loginDto.getEmail(), loginDto.getPassword());
    }

    @PostMapping("/forgot-password")
    public ApiResponse<User> forgotPassword(@Valid @RequestBody ForgotPasswordDto forgotPasswordDto) {
        return authenticationService.forgotPassword(forgotPasswordDto.getEmail());
    }

    @PostMapping("/reset-password/{token}")
    public ApiResponse<User> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto,
                                           @PathVariable @UUID(message = "Token is invalid") String token) {
        return authenticationService.resetPassword(token, resetPasswordDto.getNewPassword(), resetPasswordDto.getConfirmNewPassword());
    }
}
