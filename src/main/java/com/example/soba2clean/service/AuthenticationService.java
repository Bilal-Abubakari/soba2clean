package com.example.soba2clean.service;

import com.example.soba2clean.dto.RegisterDto;
import com.example.soba2clean.exception.BadRequestException;
import com.example.soba2clean.exception.authentication.UserAlreadyExistsException;
import com.example.soba2clean.model.ApiResponse;
import com.example.soba2clean.model.User;
import com.example.soba2clean.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ApiResponse<User> register(RegisterDto registerDto) {
        User user = new User();
        Boolean userExists = this.userRepository.existsByEmail(registerDto.getEmail());

        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }
        if (userExists) {
           throw new UserAlreadyExistsException("User with this email already exists");
        }
        user.setUser(registerDto);
        User savedUser = this.userRepository.save(user);
        return new ApiResponse<>("User registered successfully", savedUser);
    }
}
