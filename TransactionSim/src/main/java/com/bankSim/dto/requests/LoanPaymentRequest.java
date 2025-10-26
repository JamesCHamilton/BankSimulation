package com.bankSim.dto.requests;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoanPaymentRequest {
    private final Long loanId;
    private final BigDecimal paymentAmount;
    private final String paymentMethod;
    private final String paymentStatus;
    private final LocalDateTime paymentDate;

    public LoanPaymentRequest(
        @JsonProperty("loanId")Long loanId,
        @JsonProperty("paymentAmount")BigDecimal paymentAmount,
        @JsonProperty("paymentMethod")String paymentMethod,
        @JsonProperty("paymentStatus")String paymentStatus
        ) {
        this.loanId = loanId;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentDate = LocalDateTime.now();
    }

    public Long getLoanId() {
        return loanId;
    }public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }public LocalDateTime getPaymentDate() {
        return paymentDate;
    }public String getPaymentMethod() {
        return paymentMethod;
    }public String getPaymentStatus() {
        return paymentStatus;
    }

    
}
