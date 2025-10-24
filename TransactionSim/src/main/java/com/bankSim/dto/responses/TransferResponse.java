package com.bankSim.dto.responses;



import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
public class TransferResponse {
    private String status;
    private Long transactionId;
    private BigDecimal amount;
    private String message;
    private String transferType;


    public TransferResponse(
    @JsonProperty("status") String status, 
    @JsonProperty("transactionId")Long transactionId, 
    @JsonProperty("transferAmount") BigDecimal amount, 
    @JsonProperty("transferType") String transferType,
    @JsonProperty("TransferMessage") String message) {
        
        this.status = status;
        this.transactionId = transactionId;
        this.amount = amount;
        this.message = message;
        this.transferType = transferType;
    }

    public BigDecimal getAmount() {
        return amount;
    }public String getMessage() {
        return message;
    }public String getStatus() {
        return status;
    }public Long getTransactionId() {
        return transactionId;
    }public String getTransferType() {
        return transferType;
    }
}
