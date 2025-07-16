package com.example.soba2clean.response.authentication;

import com.example.soba2clean.model.User;

public class LoginResponse {
    String accessToken;
    String refreshToken;
    User user;

    public LoginResponse(String accessToken, String refreshToken, User user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public User getUser() {
        return user;
    }
}
