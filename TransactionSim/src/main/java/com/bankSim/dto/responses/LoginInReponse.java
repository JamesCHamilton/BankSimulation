package com.bankSim.dto.responses;

import org.springframework.http.HttpStatus;

public class LoginInReponse {
    private final String message;
    private final HttpStatus status;
    private final String token;

    public LoginInReponse(String token, String message, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.token = token;
    }

    public String getToken() {
        return token;
    }
    public HttpStatus getStatus() {
        return status;
    }
    public String getMessage() {
        return message;
    }
}
