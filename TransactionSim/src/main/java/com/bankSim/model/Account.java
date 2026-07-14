package com.bankSim.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity 
@Table(name = "accounts")
public class Account {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private String routingNumber;
    private BigDecimal balance;
    private Integer creditScore;
    private String accountType;
    private String bank;
    private Long userId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "account_transactions", joinColumns = @JoinColumn(name = "account_id"))
    @Column(name = "transaction_id")
    private List<Long> transactions;

    protected Account() {}

    public Account(
        @JsonProperty("userIdid")Long userid,
        @JsonProperty("accountNumber")String accountNumber, 
        @JsonProperty("accountHolder") String accountHolder,
        @JsonProperty("routingNumber")String routingNumber,
        @JsonProperty("accountType")String accountType,
        @JsonProperty("bank")String bank) {
        this.accountNumber = accountNumber;
        this.balance = BigDecimal.ZERO;
        this.routingNumber = routingNumber;
        this.accountType = accountType;
        this.bank = bank;
        this.creditScore = null;
        this.userId = userid;
        this.transactions = new ArrayList<>();
    }

    public String getAccountNumber() {return accountNumber;}
    public String getRoutingNumber() {return routingNumber;}
    public BigDecimal getBalance() {return balance;}
    public String getAccountType() {return accountType;}
    public String getBank() {return bank;}
    public Integer getCreditScore() {return creditScore;}
    public void setBalance(BigDecimal balance) {this.balance = balance;}
    public void setCreditScore(Integer creditScore) {this.creditScore = creditScore;}
    public Long getId() {return id;}
    public Long getUserId() {return userId;}
    public List<Long> getTransactions() {return transactions;}
    public void addTransaction(Long transactionId) {this.transactions.add(transactionId);}
    public void removeTransaction(Long transactionId) {this.transactions.remove(transactionId);}


}
