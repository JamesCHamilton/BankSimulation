package com.bankSim.dto.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoanPaymentReponse {
    private final String status;
    private final Long loanId;
    private final String message;
    private final BigDecimal remainingBalance;
    private final LocalDateTime paidAt;
    public LoanPaymentReponse(
        @JsonProperty("status")String status,
        @JsonProperty("loanId")Long loanId,
        @JsonProperty("remainingBalance")BigDecimal remainingBalance,
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
    }public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }public String getStatus() {
        return status;
    }public LocalDateTime getPaidAt() {
        return paidAt;
    }
}
