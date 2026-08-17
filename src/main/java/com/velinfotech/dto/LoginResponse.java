package com.velinfotech.dto;

public class LoginResponse {

    private String token;
    private String username;
    /** Seconds until the token expires, so the client can pre-empt a 401. */
    private long expiresInSeconds;

    public LoginResponse(String token, String username, long expiresInSeconds) {
        this.token = token;
        this.username = username;
        this.expiresInSeconds = expiresInSeconds;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getExpiresInSeconds() {
        return expiresInSeconds;
    }

    public void setExpiresInSeconds(long expiresInSeconds) {
        this.expiresInSeconds = expiresInSeconds;
    }
}
