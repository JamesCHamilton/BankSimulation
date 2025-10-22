package com.bankSim.dto.requests;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountCreationRequest {
    private final Long id;
    @JsonProperty("sourceAmount") private final BigDecimal amount;
    private final String accountType;
    private final String routingNumber;
    private final String accountNumber;
    

    public AccountCreationRequest(
        Long id,
        @JsonProperty("sourceAmountType")String accountType,
        @JsonProperty("sourceRoutingNumber")String routingNumber,
        @JsonProperty("sourceAmountNumber")String accountNumber)
    {
        this.id = id;
        this.amount = BigDecimal.ZERO;
        this.accountType = accountType;
        this.routingNumber = routingNumber;
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {return accountNumber;}
    public String getAccountType() {return accountType;}
    public BigDecimal getAmount() {return amount;}
    public Long getId() {return id;}
    public String getRoutingNumber() {return routingNumber;}
   }
