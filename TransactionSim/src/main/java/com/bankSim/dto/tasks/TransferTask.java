package com.bankSim.dto.tasks;

import java.math.BigDecimal;

public class TransferTask {
    private Long transferId;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private String transferType;

    public TransferTask(Long transferId, Long fromAccountId, Long toAccountId, BigDecimal amount, String transferType) {
        this.transferId = transferId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.transferType = transferType;
    }

    public Long getTransferId() { return transferId; }
    public Long getFromAccountId() { return fromAccountId; }
    public Long getToAccountId() { return toAccountId; }
    public BigDecimal getAmount() { return amount; }
    public String getTransferType() {return transferType;}

}
