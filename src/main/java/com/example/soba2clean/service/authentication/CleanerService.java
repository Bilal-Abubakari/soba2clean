package com.example.soba2clean.service.authentication;

import com.example.soba2clean.dto.cleaner.AddCleanerDto;
import com.example.soba2clean.exception.NotFoundException;
import com.example.soba2clean.model.Cleaner;
import com.example.soba2clean.model.User;
import com.example.soba2clean.repository.CleanerRepository;
import com.example.soba2clean.response.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public class CleanerService {
    private final CleanerRepository cleanerRepository;
    private final UserService userService;

    public CleanerService(CleanerRepository cleanerRepository, UserService userService) {
        this.cleanerRepository = cleanerRepository;
        this.userService = userService;
    }

    public ApiResponse<Cleaner> addCleaner(String email, AddCleanerDto addCleanerDto) {
        User user = this.userService.findUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with this email does not exist"));
        Cleaner existingCleaner = this.cleanerRepository.findCleanerByUser(user);
        if (existingCleaner != null) {
            throw new NotFoundException("Cleaner already exists for user with email: " + email);
        }
        Cleaner cleaner = new Cleaner();
        cleaner.setUser(user);
        cleaner.setCleaner(addCleanerDto);
        Cleaner savedCleaner = this.cleanerRepository.save(cleaner);

        return new ApiResponse<>("Cleaner added successfully", savedCleaner);
    }

    public ApiResponse<Cleaner> getCleanerByEmail(String email) {
        User user = this.userService.findUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with this email does not exist"));

        Cleaner cleaner = this.cleanerRepository.findCleanerByUser(user);

        if (cleaner == null) {
            throw new NotFoundException("Cleaner not found for user with email: " + email);
        }

        return new ApiResponse<>("Cleaner found successfully", cleaner);
    }
}
