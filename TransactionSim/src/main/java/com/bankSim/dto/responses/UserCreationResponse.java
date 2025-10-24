package com.bankSim.dto.responses;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserCreationResponse {
    private final Long userId;
    private final String message;
    private final String email;
    private final String userName;
    @JsonProperty("timestamp") private final LocalDateTime updatedAt;

    public UserCreationResponse(
        @JsonProperty("id") Long userId,
        @JsonProperty("userName") String userName,
        @JsonProperty("email") String email,
        @JsonProperty("message") String message) {
        this.userId = userId;
        this.message = message;
        this.email = email;
        this.userName = userName;
        this.updatedAt = LocalDateTime.now();
    }
    public String getUserName() {
        return userName;
    }
    public Long getUserId() {
        return userId;
    }
    public String getMessage() {
        return message;
    }
    public String getEmail() {
        return email;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
