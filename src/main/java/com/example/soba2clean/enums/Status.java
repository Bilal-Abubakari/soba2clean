package com.example.soba2clean.enums;

import lombok.Getter;

@Getter
public enum Status {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    PENDING("Pending"),
    SUSPENDED("Suspended");

    private final String status;

    Status(String status) {
        this.status = status;
    }

}
