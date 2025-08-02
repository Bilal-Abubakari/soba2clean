package com.example.soba2clean.service.authentication;

import com.example.soba2clean.exception.NotFoundException;
import com.example.soba2clean.model.Cleaner;
import com.example.soba2clean.model.User;
import com.example.soba2clean.repository.CleanerRepository;
import org.springframework.stereotype.Service;

@Service
public class CleanerService {
    private final CleanerRepository cleanerRepository;
    private final UserService userService;

    public CleanerService(CleanerRepository cleanerRepository, UserService userService) {
        this.cleanerRepository = cleanerRepository;
        this.userService = userService;
    }

    public Cleaner getCleanerByEmail(String email) {
        User user = this.userService.findUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with this email does not exist"));

        Cleaner cleaner = this.cleanerRepository.findCleanerByUser(user);

        if (cleaner == null) {
            throw new NotFoundException("Cleaner not found for user with email: " + email);
        }

        return cleaner;
    }
}
