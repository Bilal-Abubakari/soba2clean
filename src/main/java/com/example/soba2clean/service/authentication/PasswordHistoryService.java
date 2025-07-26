package com.example.soba2clean.service.authentication;

import com.example.soba2clean.exception.BadRequestException;
import com.example.soba2clean.model.PasswordHistory;
import com.example.soba2clean.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordHistoryService {
    private final PasswordEncoder passwordEncoder;

    public PasswordHistoryService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public PasswordHistory createPasswordHistory(String password, User user) {
        PasswordHistory passwordHistory = new PasswordHistory();
        String passwordHash = this.passwordEncoder.encode(password);
        passwordHistory.setHistoricalPasswordHash(passwordHash);
        passwordHistory.setUser(user);
        return passwordHistory;
    }

    public PasswordHistory processPassword(String password, User user) {
        boolean inHistory = user.getPasswordHistory().stream()
                .anyMatch(history -> checkIfPasswordIsCorrect(password, history.getHistoricalPasswordHash()));
        if (inHistory) {
            throw new BadRequestException("Password has been used before. Please choose a different password.");
        }
        return createPasswordHistory(password, user);
    }

    public boolean checkIfPasswordIsCorrect(String password, String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }

}
