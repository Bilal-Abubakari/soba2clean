package com.example.soba2clean.enums;

public enum EmailTemplateName {
    EMAIL_VERIFICATION("email_verification");

    private final String templateFileName;

    EmailTemplateName(String templateFileName) {
        this.templateFileName = templateFileName;
    }

    public String getTemplateFileName() {
        return templateFileName;
    }
}
