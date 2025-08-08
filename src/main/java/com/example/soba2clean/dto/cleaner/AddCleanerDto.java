package com.example.soba2clean.dto.cleaner;

import com.example.soba2clean.enums.Experience;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AddCleanerDto {
    @NotBlank(message = "License number is required")
    public String licenseNumber;

    @NotNull(message = "Experience is required")
    public Experience experience;

    @NotBlank(message = "Description is required")
    public String description;

    @Positive(message = "Rate per hour must be positive")
    public double ratePerHour;

    @NotBlank(message = "City is required")
    public String city;

    @NotBlank(message = "Country is required")
    public String country;

    @NotBlank(message = "Town is required")
    public String town;

    @NotBlank(message = "Longitude is required")
    public String longitude;

    @NotBlank(message = "Latitude is required")
    public String latitude;
}
