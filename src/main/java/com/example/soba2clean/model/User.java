package com.example.soba2clean.model;

import com.example.soba2clean.dto.RegisterDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "sobaUser")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Email is not valid")
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String password;

    @Pattern(regexp = "^(?:0\\d{9}|\\+233\\d{9})$",
             message = "Phone number must be a valid Ghanaian number starting with 0 or +233")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    private String address;

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

    public void setUser(RegisterDto registerDto) {
        firstName = registerDto.getFirstName();
        lastName = registerDto.getLastName();
        email = registerDto.getEmail();
        password = registerDto.getPassword();
        phoneNumber = registerDto.getPhoneNumber();
        address = registerDto.getAddress();
    }

}
