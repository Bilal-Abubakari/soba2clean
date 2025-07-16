package com.example.soba2clean.model;

import com.example.soba2clean.dto.authentication.RegisterDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "sobaUser")
public class User  extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    @JsonIgnore
    private String password;

    private String phoneNumber;

    private String address;

    private Instant verifiedAt;

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public Instant getVerifiedAt() {
        return  verifiedAt;
    }


    public void setUser(RegisterDto registerDto) {
        firstName = registerDto.getFirstName();
        lastName = registerDto.getLastName();
        email = registerDto.getEmail();
        phoneNumber = registerDto.getPhoneNumber();
        address = registerDto.getAddress();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setVerifiedAt() {
        verifiedAt = Instant.now();
    }
}
