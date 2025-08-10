package com.example.soba2clean.model;

import com.example.soba2clean.dto.cleaner.AddCleanerDto;
import com.example.soba2clean.enums.Experience;
import com.example.soba2clean.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class Cleaner extends AuditableEntity {
    @Setter
    @OneToOne
    private User user;

    @Column(unique = true)
    private String licenseNumber;

    private Experience experience;

    private String description;

    private double ratePerHour;

    private int hires = 0;

    private String city;

    private String country;

    private String town;

    private String longitude;

    private String latitude;

    @Setter
    private Status status = Status.PENDING;

    public void setCleaner(AddCleanerDto addCleanerDto) {
        this.licenseNumber = addCleanerDto.licenseNumber;
        this.experience = addCleanerDto.experience;
        this.description = addCleanerDto.description;
        this.ratePerHour = addCleanerDto.ratePerHour;
        this.city = addCleanerDto.city;
        this.country = addCleanerDto.country;
        this.town = addCleanerDto.town;
        this.longitude = addCleanerDto.longitude;
        this.latitude = addCleanerDto.latitude;
    }

    public void incrementHires() {
        this.hires++;
    }
}
