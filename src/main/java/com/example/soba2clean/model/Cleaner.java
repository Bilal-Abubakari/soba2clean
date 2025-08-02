package com.example.soba2clean.model;

import com.example.soba2clean.enums.Experience;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

@Entity
public class Cleaner extends AuditableEntity {
    @OneToOne
    private User user;

    private String licenseNumber;

    private Experience experience;

    private String description;

    private double ratePerHour;

    private int hires;

    private String city;

    private String country;

    private String town;
}
