package com.appescritorio.backend.controller.dto;

public class AuthResponse {

    private String message;
    private String username;
    private String role;
    private String accessToken;
    private String refreshToken;

    public AuthResponse(String message, String username, String role, String accessToken, String refreshToken) {
        this.message = message;
        this.username = username;
        this.role = role;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
