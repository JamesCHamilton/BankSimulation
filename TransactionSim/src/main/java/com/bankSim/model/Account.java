package com.bankSim.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

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
    
    @ManyToOne
    private User user;

    
    protected Account() {}

    public Account(String accountNumber, User accountHolder, String routingNumber, String accountType, String bank) {
        this.accountNumber = accountNumber;
        this.balance = BigDecimal.ZERO;
        this.routingNumber = routingNumber;
        this.accountType = accountType;
        this.bank = bank;
        this.creditScore = null;
    }

    public String getAccountNumber() {return accountNumber;}
    public String getRoutingNumber() {return routingNumber;}
    public BigDecimal getBalance() {return balance;}
    public String getAccountType() {return accountType;}
    public String getBank() {return bank;}
    public Integer getCreditScore() {return creditScore;}
    public void setBalance(BigDecimal balance) {this.balance = balance;}
    public void setCreditScore(Integer creditScore) {this.creditScore = creditScore;}

}
