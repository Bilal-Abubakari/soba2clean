package com.example.soba2clean.controller;

import com.example.soba2clean.model.Cleaner;
import com.example.soba2clean.service.authentication.CleanerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cleaner")
public class CleanerController {
    private final CleanerService cleanerService;

    public CleanerController(CleanerService cleanerService) {
        this.cleanerService = cleanerService;
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping()
    public Cleaner getCleaner(Authentication authentication) {
        return this.cleanerService.getCleanerByEmail(authentication.getName());
    }
}
