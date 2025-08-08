package com.example.soba2clean.enums;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ADMIN"),
    CUSTOMER("CUSTOMER"),
    CLEANER("CLEANER");

    private final String role;

    Role(String role) {
        this.role = role;
    }

}
