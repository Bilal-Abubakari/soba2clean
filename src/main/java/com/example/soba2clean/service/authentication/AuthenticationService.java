package com.example.soba2clean.service.authentication;

import com.example.soba2clean.dto.authentication.RegisterDto;
import com.example.soba2clean.exception.BadRequestException;
import com.example.soba2clean.exception.authentication.UnauthorizedException;
import com.example.soba2clean.exception.authentication.UserAlreadyExistsException;
import com.example.soba2clean.response.ApiResponse;
import com.example.soba2clean.model.User;
import com.example.soba2clean.repository.UserRepository;
import com.example.soba2clean.response.authentication.LoginResponse;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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
        user.setPassword(this.passwordEncoder.encode(registerDto.getPassword()));
        User savedUser = this.userRepository.save(user);
        return new ApiResponse<>("User registered successfully", savedUser);
    }

    public ApiResponse<LoginResponse> login(String email, String password) {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));
        System.out.println("User found? " + user);

        if (!this.passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        LoginResponse loginResponse = new LoginResponse(jwtService.generateAccessToken(user),
                jwtService.generateRefreshToken(user) );

        return new ApiResponse<>("Login successful", loginResponse);
    }
}
