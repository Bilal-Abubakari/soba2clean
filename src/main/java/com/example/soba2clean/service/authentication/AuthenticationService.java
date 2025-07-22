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
    private final VerificationService verificationService;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, VerificationService verificationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.verificationService = verificationService;
    }

    public ApiResponse<User> register(RegisterDto registerDto) {
       try {
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
           this.verificationService.sendVerificationEmail(savedUser);
           return new ApiResponse<>("User registered successfully", savedUser);
       } catch (Exception ex) {
           throw new RuntimeException("Something went wrong creating new user: " + ex.getMessage(), ex);
       }
    }

    public ApiResponse<LoginResponse> login(String email, String password) {
       try {
           User user = this.userRepository.findByEmail(email)
                   .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

           if (user.getVerifiedAt() == null) {
                this.verificationService.sendVerificationEmail(user);
                throw new UnauthorizedException("Email not verified, please check your email for verification link");
           }

           if (!this.passwordEncoder.matches(password, user.getPassword())) {
               throw new UnauthorizedException("Invalid email or password");
           }

           String accessToken = jwtService.generateAccessToken(user);
           String refreshToken = jwtService.generateRefreshToken(user);

           LoginResponse loginResponse = new LoginResponse(accessToken,
                   refreshToken, user );

           return new ApiResponse<>("Login successful", loginResponse);
       } catch (Exception ex) {
           throw new RuntimeException("Something went wrong when authenticating user: " + ex.getMessage(), ex);
       }
    }
}
