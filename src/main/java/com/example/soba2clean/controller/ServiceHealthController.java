package com.example.soba2clean.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceHealthController {
    @GetMapping("/health")
    public String health() {
        return "Service is up and running!";
    }
}
