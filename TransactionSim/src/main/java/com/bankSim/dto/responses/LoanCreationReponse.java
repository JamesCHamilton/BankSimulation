package com.bankSim.dto.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoanCreationReponse {
    private final String status;
    private final Long loanId;
    private final String message;
    private final BigDecimal approvedAmount;
    @JsonProperty("timestamp") private final LocalDateTime createdAt;

    public LoanCreationReponse(
        @JsonProperty("status")String status,
        @JsonProperty("loanId")Long loanId,
        @JsonProperty("approvedAmount")BigDecimal approvedAmount,
        @JsonProperty("message")String message
        ) {
        this.status = status;
        this.loanId = loanId;
        this.approvedAmount = approvedAmount;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    public BigDecimal getApprovedAmount() {
        return approvedAmount;
    }public Long getLoanId() {
        return loanId;
    }public String getMessage() {
        return message;
    }public String getStatus() {
        return status;
    }public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
