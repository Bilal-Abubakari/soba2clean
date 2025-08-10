package com.example.soba2clean.enums;

import lombok.Getter;

@Getter
public enum EmailTemplateName {
    EMAIL_VERIFICATION("email_verification"),
    FORGOT_PASSWORD("forgot_password"),
    NEW_CLEANER("new_cleaner"),
    CLEANER_APPROVED("cleaner_approved");


    private final String templateFileName;

    EmailTemplateName(String templateFileName) {
        this.templateFileName = templateFileName;
    }

}
