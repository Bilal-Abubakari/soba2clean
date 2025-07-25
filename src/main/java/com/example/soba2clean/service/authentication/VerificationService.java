package com.example.soba2clean.service.authentication;

import com.example.soba2clean.constants.AppConstants;
import com.example.soba2clean.enums.EmailTemplateName;
import com.example.soba2clean.enums.VerificationType;
import com.example.soba2clean.exception.TooManyRequestsException;
import com.example.soba2clean.exception.authentication.UnauthorizedException;
import com.example.soba2clean.mail.EmailService;
import com.example.soba2clean.model.User;
import com.example.soba2clean.model.Verification;
import com.example.soba2clean.repository.UserRepository;
import com.example.soba2clean.repository.VerificationRepository;
import com.example.soba2clean.response.ApiResponse;
import com.google.common.util.concurrent.RateLimiter;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VerificationService {
    private final ConcurrentHashMap<String, RateLimiter> userEmailRateLimiters = new ConcurrentHashMap<>();
    private final UserRepository userRepository;
    private final VerificationRepository verificationRepository;
    private final EmailService emailService;
    @Value("${app.base-url}")
    private String baseUrl;

    public VerificationService(UserRepository userRepository, VerificationRepository verificationRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.verificationRepository = verificationRepository;
        this.emailService = emailService;
    }

    public ApiResponse<User> verifyEmail(String token) {
        try {
            Verification verification = getVerificationFromToken(token, "Invalid token, please login again to receive a new verification email",
                    AppConstants.EMAIL_VERIFICATION_EXPIRY_TIME_IN_MINUTES, "Token has expired, please login again to receive a new verification email");
            User user = verification.getUser();
            user.markAsVerified();

            userRepository.save(user);

            verificationRepository.delete(verification);

            return new ApiResponse<>("Email verified successfully", user);
        } catch (Exception ex) {
            throw new RuntimeException("Something went wrong during email verification: " + ex.getMessage(), ex);
        }
    }

    public void sendVerificationEmail(User user) throws MessagingException {
        verificationRateLimiter(user.getEmail());
        Verification verification = new Verification();
        verification.setVerification(user, VerificationType.EMAIL_VERIFICATION);
        verificationRepository.save(verification);
        Map<String, Object> emailVariables = Map.of(
                "firstName", user.getFirstName(),
                "verificationLink", baseUrl + "/verification/email/" + verification.getToken());
        emailService.sendHtmlEmail(user.getEmail(), "Next Step: Email Verification", EmailTemplateName.EMAIL_VERIFICATION, emailVariables);
    }

    public void sendForgotPasswordEmail(User user) throws MessagingException {
        verificationRateLimiter(user.getEmail());
        Verification verification = new Verification();
        verification.setVerification(user, VerificationType.PASSWORD_RESET);
        verificationRepository.save(verification);
        Map<String, Object> emailVariables = Map.of(
                "firstName", user.getFirstName(),
                "resetPasswordLink", baseUrl + "/verification/forgot-password/" + verification.getToken());
        emailService.sendHtmlEmail(user.getEmail(), "Reset Your Password", EmailTemplateName.FORGOT_PASSWORD, emailVariables);
    }

    public Verification getVerificationFromToken(String token, String errorMessage, int expiryDurationInMinutes, String expiryErrorMessage) {
        Verification verification = verificationRepository.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException(errorMessage));
        Duration timeElapsed = Duration.between(verification.getCreatedAt(), Instant.now());
        if (timeElapsed.toMinutes() > expiryDurationInMinutes) {
            throw new UnauthorizedException(expiryErrorMessage);
        }
        return verification;
    }


    private void verificationRateLimiter(String email) {
        RateLimiter userLimiter = userEmailRateLimiters.computeIfAbsent(email, k -> RateLimiter.create(AppConstants.PERMITS_PER_MINUTUES_IN_SECONDS));
        if (!userLimiter.tryAcquire()) {
            throw new TooManyRequestsException("Too many verification email requests. Please wait and try again.");
        }
    }

}