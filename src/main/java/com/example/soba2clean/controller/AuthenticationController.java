package com.example.soba2clean.controller;

import com.example.soba2clean.dto.RegisterDto;
import com.example.soba2clean.model.ApiResponse;
import com.example.soba2clean.model.User;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.soba2clean.repository.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserRepository userRepository;

    public AuthenticationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @PostMapping("/register")
    public ApiResponse<User> register(@Valid @RequestBody RegisterDto registerDto) {
        User user = new User();
        user.setUser(registerDto);
        User savedUser = this.userRepository.save(user);
       return new ApiResponse<User>("User registered successfully", savedUser);
    }
}
