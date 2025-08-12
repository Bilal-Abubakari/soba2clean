package com.example.soba2clean.service;

import com.example.soba2clean.enums.EmailTemplateName;
import com.example.soba2clean.mail.EmailService;
import com.example.soba2clean.model.Cleaner;
import com.example.soba2clean.service.authentication.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService {
    private final UserService userService;
    private final EmailService emailService;
    @Value("${app.frontend-url}")
    private String frontendUrl;

    public AdminService(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    public void notifyAdminsAboutCleanerUpdate(Cleaner cleaner, Boolean newCleaner) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("cleanerName", cleaner.getUser().getFirstName() + " " + cleaner.getUser().getLastName());
        variables.put("cleanerEmail", cleaner.getUser().getEmail());
        variables.put("submittedAt", cleaner.getCreatedAt().toString());
        variables.put("dashboardUrl", this.frontendUrl + "/admin/cleaners/" + cleaner.getId() + "/approve");

        this.userService.findAllAdmins().forEach(admin -> {
            try {
                variables.put("adminName", admin.getFirstName());
                this.emailService.sendHtmlEmail(admin.getEmail(),
                        "Action required:" + (newCleaner ? "New cleaner awaiting approval" : "Cleaner profile updated, awaiting review"),
                        EmailTemplateName.NEW_CLEANER, variables);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
