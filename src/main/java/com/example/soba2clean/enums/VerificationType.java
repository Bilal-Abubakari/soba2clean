package com.example.soba2clean.enums;

import lombok.Getter;

@Getter
public enum VerificationType {
    EMAIL_VERIFICATION("Email Verification"),
    PASSWORD_RESET("Password Reset"),
    UNLOCK_ACCOUNT("Unlock Account");

    private final String typeName;

    VerificationType(String typeName) {
        this.typeName = typeName;
    }

}
