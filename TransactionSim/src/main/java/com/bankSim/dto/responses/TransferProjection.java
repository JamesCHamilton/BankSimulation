package com.bankSim.dto.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransferProjection {
    Long getId();
    Long getFromAccountId();
    Long getToAccountId();
    BigDecimal getAmount();
    String getStatus();
    String getMessage();
    LocalDateTime getCreatedAt();
    String getTransferType();
}
