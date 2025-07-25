package com.example.soba2clean.service.authentication;

import com.example.soba2clean.constants.AppConstants;
import com.example.soba2clean.dto.authentication.RegisterDto;
import com.example.soba2clean.exception.BadRequestException;
import com.example.soba2clean.exception.NotFoundException;
import com.example.soba2clean.exception.authentication.UnauthorizedException;
import com.example.soba2clean.exception.authentication.UserAlreadyExistsException;
import com.example.soba2clean.model.User;
import com.example.soba2clean.repository.UserRepository;
import com.example.soba2clean.response.ApiResponse;
import com.example.soba2clean.response.authentication.LoginResponse;
import jakarta.mail.MessagingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

            checkIfPasswordsMatch(registerDto.getPassword(), registerDto.getConfirmPassword());

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

            checkIfEmailIsVerified(user);

            if (!this.passwordEncoder.matches(password, user.getPassword())) {
                throw new UnauthorizedException("Invalid email or password");
            }

            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            LoginResponse loginResponse = new LoginResponse(accessToken,
                    refreshToken, user);

            return new ApiResponse<>("Login successful", loginResponse);
        } catch (Exception ex) {
            throw new RuntimeException("Something went wrong when authenticating user: " + ex.getMessage(), ex);
        }
    }

    public ApiResponse<User> forgotPassword(String email) {
        try {
            User user = this.userRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("User with this email does not exist"));

            checkIfEmailIsVerified(user);

            this.verificationService.sendForgotPasswordEmail(user);

            return new ApiResponse<>("Forgot password email sent successfully", user);

        } catch (Exception ex) {
            throw new RuntimeException("Something went wrong when processing forgot password: " + ex.getMessage(), ex);
        }
    }

    public ApiResponse<User> resetPassword(String token, String newPassword, String confirmNewPassword) {
        checkIfPasswordsMatch(newPassword, confirmNewPassword);
        try {
            checkIfPasswordsMatch(newPassword, confirmNewPassword);
            String errorMessage = "Invalid token, or token has expired, please try to reset your password again";
            User user = this.verificationService.getVerificationFromToken(token,
                    errorMessage,
                    AppConstants.RESET_PASSWORD_EXPIRY_TIME_IN_MINUTES,
                    errorMessage).getUser();
            user.setPassword(this.passwordEncoder.encode(newPassword));
            User updatedUser = this.userRepository.save(user);
            return new ApiResponse<>("Password reset successfully", updatedUser);
        } catch (Exception ex) {
            throw new RuntimeException("Something went wrong when resetting password: " + ex.getMessage(), ex);
        }
    }

    private void checkIfPasswordsMatch(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new BadRequestException("Passwords do not match");
        }
    }

    private void checkIfEmailIsVerified(User user) throws MessagingException {
        if (user.getVerifiedAt() == null) {
            this.verificationService.sendVerificationEmail(user);
            throw new UnauthorizedException("Email not verified, please check your email for verification link");
        }
    }
}
