package com.bankSim.dto.responses;

import org.springframework.http.HttpStatus;

public class LoginInReponse {
    private final String message;
    private final HttpStatus status;

    public LoginInReponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
    public String getMessage() {
        return message;
    }
}
