package com.bankSim.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginInRequest {
    private final String username;
    private final String password;

    public LoginInRequest(
        @JsonProperty("username")String username, 
        @JsonProperty("password")String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }public String getUsername() {
        return username;
    }
}
