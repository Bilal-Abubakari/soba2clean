package com.example.soba2clean.response.authentication;

public class LoginResponse {
    String accessToken;
    String refreshToken;

    public LoginResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
