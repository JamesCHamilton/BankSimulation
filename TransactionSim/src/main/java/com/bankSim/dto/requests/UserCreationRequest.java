package com.bankSim.dto.requests;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserCreationRequest {
    private final Long userId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String userName;
    private final String password;
    private final List<Long> accountIds;
    private final List<Long> loanIds;
    @JsonProperty("timestamp") private final LocalDateTime timestamp;


    public UserCreationRequest(
        @JsonProperty("userId")Long userId,
        @JsonProperty("firstName")String firstName,
        @JsonProperty("lastName")String lastName,
        @JsonProperty("email")String email,
        @JsonProperty("userName")String userName,
        @JsonProperty("password")String password,
        @JsonProperty("accountIds")List<Long> accountIds,
        @JsonProperty("loanIds")List<Long> loanIds) {
            this.userId = userId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.userName = userName;
            this.password = password;
            this.accountIds = accountIds;
            this.loanIds = loanIds;
            this.timestamp = LocalDateTime.now();
    }

    public List<Long> getAccountIds() {
        return accountIds;
    }public String getEmail() {
        return email;
    }public String getFirstName() {
        return firstName;
    }public String getLastName() {
        return lastName;
    }public List<Long> getLoanIds() {
        return loanIds;
    }public String getPassword() {
        return password;
    }public Long getUserId() {
        return userId;
    }public String getUserName() {
        return userName;
    }
}
