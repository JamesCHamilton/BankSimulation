package com.bankSim.dto.requests;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
public class TransferRequest {
    private final Long fromAccountId;
    private final Long toAccountId;
    private final BigDecimal amount;
    private Long transferId;
    private String transferType;
    @JsonProperty("timestamp") private final LocalDateTime timestamp;

    public TransferRequest(
        @JsonProperty("TransferId")Long transferId, 
        @JsonProperty("fromAccountId")Long fromAccountId, 
        @JsonProperty("toAccountId")Long toAccountId, 
        @JsonProperty("transferAmount")BigDecimal amount,
        @JsonProperty("transferType")String transferType) {

        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public LocalDateTime getTimeStamp(){return timestamp;}
    public Long getFromAccountId() {return fromAccountId;}
    public Long getToAccountId() {return toAccountId;}
    public BigDecimal getAmount() {return amount;}
    public Long getTransferId() {return transferId;}
    public String getTransferType() {return transferType;}

}
