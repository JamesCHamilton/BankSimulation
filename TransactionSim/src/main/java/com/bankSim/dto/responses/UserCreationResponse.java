package com.bankSim.dto.responses;

public class UserCreationResponse {
    private final Long userId;
    private final String message;
    private final String email;
    private final String userName;
    public UserCreationResponse(Long userId,String userName, String email, String message) {
        this.userId = userId;
        this.message = message;
        this.email = email;
        this.userName = userName;
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
}
