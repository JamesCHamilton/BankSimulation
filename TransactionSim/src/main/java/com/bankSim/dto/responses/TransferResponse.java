package com.bankSim.dto.responses;



import java.math.BigDecimal;
public class TransferResponse {
    private String status;
    private String transactionId;
    private BigDecimal amount;
    private String message;

    public TransferResponse(String status, String transactionId, BigDecimal amount, String message) {
        this.status = status;
        this.transactionId = transactionId;
        this.amount = amount;
        this.message = message;
    }

    public BigDecimal getAmount() {
        return amount;
    }public String getMessage() {
        return message;
    }public String getStatus() {
        return status;
    }public String getTransactionId() {
        return transactionId;
    }   
}
