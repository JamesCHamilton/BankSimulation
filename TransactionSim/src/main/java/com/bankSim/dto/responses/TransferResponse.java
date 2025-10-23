package com.bankSim.dto.responses;



import java.math.BigDecimal;
public class TransferResponse {
    private String status;
    private Long transactionId;
    private BigDecimal amount;
    private String message;
    private String transferType;


    public TransferResponse(String status, Long transactionId, BigDecimal amount, String transferType,String message) {
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
    }   
}
