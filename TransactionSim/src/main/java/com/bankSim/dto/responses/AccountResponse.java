package com.bankSim.dto.responses;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountResponse {
    private final Long accountId;
    private final String accountNumber;
    private final String accountType;
    private final double balance;
    private final String accountHolder;
    @JsonProperty("timestamp") private final LocalDateTime updatedAt;

    public AccountResponse(
        @JsonProperty("id") Long accountId,
        @JsonProperty("accountNumber") String accountNumber,
        @JsonProperty("accountType") String accountType,
        @JsonProperty("balance") double balance,
        @JsonProperty("accountHolder") String accountHolder
        ) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.accountHolder= accountHolder;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountHolder() {
        return accountHolder;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

}
