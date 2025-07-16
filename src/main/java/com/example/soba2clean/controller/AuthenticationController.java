package com.example.soba2clean.controller;

import com.example.soba2clean.dto.authentication.LoginDto;
import com.example.soba2clean.dto.authentication.RegisterDto;
import com.example.soba2clean.model.User;
import com.example.soba2clean.response.ApiResponse;
import com.example.soba2clean.response.authentication.LoginResponse;
import com.example.soba2clean.service.authentication.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
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
}
