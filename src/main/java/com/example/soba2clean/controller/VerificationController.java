package com.example.soba2clean.controller;

import com.example.soba2clean.model.User;
import com.example.soba2clean.response.ApiResponse;
import com.example.soba2clean.service.authentication.VerificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verification")
public class VerificationController {
    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @GetMapping("/email/{token}")
    public ApiResponse<User> verifyEmail(@PathVariable String token) {
        return verificationService.verifyEmail(token);
    }
}
