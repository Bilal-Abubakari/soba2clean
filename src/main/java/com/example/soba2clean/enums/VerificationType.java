package com.example.soba2clean.enums;

public enum VerificationType {
    EMAIL_VERIFICATION("Email Verification"),
    PASSWORD_RESET("Password Reset");

    private final String typeName;

    VerificationType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
