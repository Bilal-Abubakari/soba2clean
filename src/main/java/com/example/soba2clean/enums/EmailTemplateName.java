package com.example.soba2clean.enums;

public enum EmailTemplateName {
    EMAIL_VERIFICATION("email_verification"),
    FORGOT_PASSWORD("forgot_password");

    private final String templateFileName;

    EmailTemplateName(String templateFileName) {
        this.templateFileName = templateFileName;
    }

    public String getTemplateFileName() {
        return templateFileName;
    }
}
