package com.example.soba2clean.service.authentication;

import com.example.soba2clean.constants.AppConstants;
import com.example.soba2clean.dto.authentication.RegisterDto;
import com.example.soba2clean.enums.Role;
import com.example.soba2clean.exception.BadRequestException;
import com.example.soba2clean.exception.NotFoundException;
import com.example.soba2clean.exception.authentication.UnauthorizedException;
import com.example.soba2clean.exception.authentication.UserAlreadyExistsException;
import com.example.soba2clean.model.PasswordHistory;
import com.example.soba2clean.model.User;
import com.example.soba2clean.model.Verification;
import com.example.soba2clean.repository.UserRepository;
import com.example.soba2clean.response.ApiResponse;
import com.example.soba2clean.response.authentication.LoginResponse;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final VerificationService verificationService;
    private final PasswordHistoryService passwordHistoryService;
    private final UserService userService;

    public AuthenticationService(UserRepository userRepository, JwtService jwtService, VerificationService verificationService, PasswordHistoryService passwordHistoryService, UserService userService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.verificationService = verificationService;
        this.passwordHistoryService = passwordHistoryService;
        this.userService = userService;
    }

    public ApiResponse<User> register(RegisterDto registerDto, Role role, boolean mustChangePassword) {
        try {
            User user = new User();

            boolean userExists = this.userService.userExistsByEmail(registerDto.getEmail());

            if (userExists) {
                throw new UserAlreadyExistsException("User with this email already exists");
            }

            checkIfPasswordsMatch(registerDto.getPassword(), registerDto.getConfirmPassword());
            user.setUser(registerDto, role);
            PasswordHistory passwordHistory = this.passwordHistoryService.createPasswordHistory(registerDto.getPassword(), user);
            user.setPassword(passwordHistory.getHistoricalPasswordHash());
            user.addPasswordHistory(passwordHistory);
            user.setMustChangePassword(mustChangePassword);
            if (role.equals(Role.ADMIN))
                user.markAsVerified();
            User savedUser = this.userRepository.save(user);
            if (!role.equals(Role.ADMIN))
                this.verificationService.sendVerificationEmail(savedUser);
            return new ApiResponse<>("User registered successfully", savedUser);
        } catch (Exception ex) {
            throw new RuntimeException("Something went wrong creating new user: " + ex.getMessage(), ex);
        }
    }

    public ApiResponse<User> register(RegisterDto registerDto, Role role) {
        return this.register(registerDto, role, false);
    }

    public ApiResponse<LoginResponse> login(String email, String password) {
        try {
            User user = this.userRepository.findByEmail(email)
                    .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

            checkIfEmailIsVerified(user);

            if (!this.passwordHistoryService.checkIfPasswordIsCorrect(password, user.getPassword())) {
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
        try {
            checkIfPasswordsMatch(newPassword, confirmNewPassword);
            String errorMessage = "Invalid token, or token has expired, please try to reset your password again";

            Verification verification = this.verificationService.getVerificationFromToken(token,
                    errorMessage,
                    AppConstants.RESET_PASSWORD_EXPIRY_TIME_IN_MINUTES,
                    errorMessage);
            User user = verification.getUser();
            PasswordHistory processedPasswordHistory = this.passwordHistoryService.processPassword(newPassword, user);
            user.setPassword(processedPasswordHistory.getHistoricalPasswordHash());
            user.addPasswordHistory(processedPasswordHistory);
            User updatedUser = this.userRepository.save(user);
            this.verificationService.deleteVerification(verification);
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
