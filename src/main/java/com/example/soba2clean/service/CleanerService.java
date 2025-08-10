package com.example.soba2clean.service;

import com.example.soba2clean.dto.cleaner.AddCleanerDto;
import com.example.soba2clean.enums.EmailTemplateName;
import com.example.soba2clean.enums.Status;
import com.example.soba2clean.exception.NotFoundException;
import com.example.soba2clean.mail.EmailService;
import com.example.soba2clean.model.Cleaner;
import com.example.soba2clean.model.User;
import com.example.soba2clean.repository.CleanerRepository;
import com.example.soba2clean.response.ApiResponse;
import com.example.soba2clean.service.authentication.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CleanerService {
    @Value("${app.frontend-url}")
    private String frontendUrl;
    private final CleanerRepository cleanerRepository;
    private final UserService userService;
    private final AdminService adminService;
    private final EmailService emailService;

    public CleanerService(CleanerRepository cleanerRepository, UserService userService, AdminService adminService, EmailService emailService) {
        this.cleanerRepository = cleanerRepository;
        this.userService = userService;
        this.adminService = adminService;
        this.emailService = emailService;
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
        cleaner.setStatus(Status.PENDING);
        cleaner.setCleaner(addCleanerDto);
        Cleaner savedCleaner = this.cleanerRepository.save(cleaner);
        this.adminService.notifyAdminsAboutNewCleaner(savedCleaner);
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

    public ApiResponse<Cleaner> approveCleaner(String id) {
        Cleaner cleaner = this.cleanerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cleaner with this ID does not exist"));

        cleaner.setStatus(Status.ACTIVE);
        Cleaner updatedCleaner = this.cleanerRepository.save(cleaner);
        this.sendApprovalNotification(updatedCleaner);
        return new ApiResponse<>("Cleaner approved successfully", updatedCleaner);
    }

    private void sendApprovalNotification(Cleaner cleaner) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("cleanerName", cleaner.getUser().getFirstName() + " " + cleaner.getUser().getLastName());
        variables.put("profileUrl",  this.frontendUrl + "/cleaners/" + cleaner.getId());
        variables.put("supportEmail", "support@example.com");
        variables.put("currentYear", String.valueOf(java.time.Year.now()));
        try {
            this.emailService.sendHtmlEmail(cleaner.getUser().getEmail(), "You're approved! Your Soba2Clean profile is live",
                    EmailTemplateName.CLEANER_APPROVED, variables);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send approval notification email", e);
        }
    }
}
