package com.bankSim.controller;

import com.bankSim.dto.requests.LoginInRequest;
import com.bankSim.dto.requests.UserCreationRequest;
import com.bankSim.dto.responses.LoginInReponse;
import com.bankSim.dto.responses.UserCreationResponse;
import com.bankSim.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserCreationResponse> register(@RequestBody UserCreationRequest request) {
        UserCreationResponse response = userService.CreateUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginInReponse> login(@RequestBody LoginInRequest request) {
        LoginInReponse response = userService.loginUser(request.getUsername(), request.getPassword());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
