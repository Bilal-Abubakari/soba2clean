package com.example.soba2clean.controller;

import com.example.soba2clean.dto.cleaner.AddCleanerDto;
import com.example.soba2clean.model.Cleaner;
import com.example.soba2clean.response.ApiResponse;
import com.example.soba2clean.service.authentication.CleanerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cleaner")
public class CleanerController {
    private final CleanerService cleanerService;

    public CleanerController(CleanerService cleanerService) {
        this.cleanerService = cleanerService;
    }

    @PreAuthorize("hasAuthority('ROLE_CLEANER')")
    @PostMapping()
    public ApiResponse<Cleaner> addCleaner(@RequestBody AddCleanerDto addCleanerDto, Authentication authentication) {
        return this.cleanerService.addCleaner(authentication.getName(), addCleanerDto);
    }

    @PreAuthorize("hasAuthority('ROLE_CLEANER')")
    @GetMapping()
    public ApiResponse<Cleaner> getCleaner(Authentication authentication) {
        return this.cleanerService.getCleanerByEmail(authentication.getName());
    }
}
