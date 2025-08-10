package com.example.soba2clean.controller;

import com.example.soba2clean.dto.cleaner.AddCleanerDto;
import com.example.soba2clean.model.Cleaner;
import com.example.soba2clean.response.ApiResponse;
import com.example.soba2clean.service.CleanerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cleaners")
public class CleanerController {
    private final CleanerService cleanerService;

    public CleanerController(CleanerService cleanerService) {
        this.cleanerService = cleanerService;
    }

    @PreAuthorize("hasAuthority('ROLE_CLEANER')")
    @PostMapping("/me")
    public ApiResponse<Cleaner> addCleaner(@RequestBody AddCleanerDto addCleanerDto, Authentication authentication) {
        return this.cleanerService.addCleaner(authentication.getName(), addCleanerDto);
    }

    @PreAuthorize("hasAuthority('ROLE_CLEANER')")
    @GetMapping("/me")
    public ApiResponse<Cleaner> getCleaner(Authentication authentication) {
        return this.cleanerService.getCleanerByEmail(authentication.getName());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}/approve")
    public ApiResponse<Cleaner> approveCleaner(@PathVariable String id) {
        return this.cleanerService.approveCleaner(id);
    }
}
