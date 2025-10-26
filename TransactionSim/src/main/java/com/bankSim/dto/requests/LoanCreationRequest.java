package com.bankSim.dto.requests;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoanCreationRequest {
    private final Long userId;
    private final BigDecimal amount;
    private final int termInMonths;
    private final double interestRate;
    private final String loanType;
    private final String status;
    private final LocalDateTime createdAt;
    
    public LoanCreationRequest(
        @JsonProperty("userId")Long userId,
        @JsonProperty("amount")BigDecimal amount,
        @JsonProperty("termInMonths")int termInMonths,
        @JsonProperty("interestRate")double interestRate,
        @JsonProperty("loanType")String loanType,
        @JsonProperty("status")String status
        ) {
        this.userId = userId;
        this.amount = amount;
        this.termInMonths = termInMonths;
        this.interestRate = interestRate;
        this.loanType = loanType;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public Long getUserId() {
        return userId;
    }
    public BigDecimal getAmount() {
        return amount;
    }public double getInterestRate() {
        return interestRate;
    }public String getLoanType() {
        return loanType;
    }public String getStatus() {
        return status;
    }public int getTermInMonths() {
        return termInMonths;
    }public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
