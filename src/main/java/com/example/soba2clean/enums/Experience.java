package com.example.soba2clean.enums;

import lombok.Getter;

@Getter
public enum Experience {
    TOP_PRO("Top Pro"),
    PRO("Pro"),
    BASIC("Basic"),
    NEW("New");

    private final String experienceLevel;

     Experience(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

}
