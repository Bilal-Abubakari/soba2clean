package com.example.soba2clean.enums;

import lombok.Getter;

@Getter
public enum Status {
    ACTIVE("Active"), // When the cleaner is available for work
    INACTIVE("Inactive"), // When the cleaner is not available for work
    PENDING("Pending"), // When the cleaner's application is under review
    SUSPENDED("Suspended"); // When the cleaner's account is temporarily suspended by an admin

    private final String status;

    Status(String status) {
        this.status = status;
    }

}
