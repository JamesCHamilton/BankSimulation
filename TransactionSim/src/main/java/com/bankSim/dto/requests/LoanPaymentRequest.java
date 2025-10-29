package com.bankSim.dto.requests;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoanPaymentRequest {
    private final Long loanId;
    private final double paymentAmount;
    private final String paymentMethod;
    private final String paymentStatus;
    private final LocalDateTime paymentDate;

    public LoanPaymentRequest(
        @JsonProperty("loanId")Long loanId,
        @JsonProperty("paymentAmount")double paymentAmount,
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
    }public double getPaymentAmount() {
        return paymentAmount;
    }public LocalDateTime getPaymentDate() {
        return paymentDate;
    }public String getPaymentMethod() {
        return paymentMethod;
    }public String getPaymentStatus() {
        return paymentStatus;
    }

    
}
