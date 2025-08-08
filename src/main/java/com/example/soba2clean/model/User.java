package com.example.soba2clean.model;

import com.example.soba2clean.dto.authentication.RegisterDto;
import com.example.soba2clean.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "sobaUser")
public class User extends AuditableEntity implements UserDetails {
    @Getter
    private String firstName;

    @Getter
    private String lastName;

    @Getter
    private String email;

    @Setter
    @Getter
    @JsonIgnore
    private String password;

    @Getter
    private String phoneNumber;

    @Getter
    private String address;

    @Setter
    @Getter
    private boolean mustChangePassword = false;

    @Enumerated(EnumType.STRING)
    @Getter
    private Role role;

    @Getter
    private Instant verifiedAt;

    @Getter
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PasswordHistory> passwordHistory = new ArrayList<>();

    public void setUser(RegisterDto registerDto, Role role) {
        firstName = registerDto.getFirstName();
        lastName = registerDto.getLastName();
        email = registerDto.getEmail();
        phoneNumber = registerDto.getPhoneNumber();
        address = registerDto.getAddress();
        this.role = role;
    }

    public void markAsVerified() {
        verifiedAt = Instant.now();
    }

    public void addPasswordHistory(PasswordHistory passwordHistory) {
        this.passwordHistory.add(passwordHistory);
        passwordHistory.setUser(this);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getRole());

        return List.of(authority);
    }

    @Override
    public String getUsername() {
        return email;
    }
}
