package com.bankSim.dto.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoanPaymentReponse {
    private final String status;
    private final Long loanId;
    private final String message;
    private final double remainingBalance;
    private final LocalDateTime paidAt;
    public LoanPaymentReponse(
        @JsonProperty("status")String status,
        @JsonProperty("loanId")Long loanId,
        @JsonProperty("remainingBalance")double remainingBalance,
        @JsonProperty("message")String message
        ) {
        this.status = status;
        this.loanId = loanId;
        this.remainingBalance = remainingBalance;
        this.message = message;
        this.paidAt = LocalDateTime.now();
    }
    public Long getLoanId() {
        return loanId;
    }public String getMessage() {
        return message;
    }public double getRemainingBalance() {
        return remainingBalance;
    }public String getStatus() {
        return status;
    }public LocalDateTime getPaidAt() {
        return paidAt;
    }
}
