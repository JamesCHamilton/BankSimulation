package com.bankSim.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "loans")
public class Loan {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loanId;
    private String userId;
    private double princaipalAmount;
    private double interestRate;
    private int termMonths;
    private double balance;

    protected Loan() {}

    public Loan(String loanId, String userId, double balance,double princaipalAmount, double interestRate, int termMonths) {
        this.loanId = loanId;
        this.userId = userId;
        this.balance = balance;
        this.interestRate = interestRate;
        this.termMonths = termMonths;
    }

    public String getLoanId() {return loanId;}
    public String getUserId() {return userId;}
    public double getInterestRate() {return interestRate;}
    public int getTermMonths() {return termMonths;}
    public double getBalance() {return balance;}
    public double getLoanAmount() {return princaipalAmount;}
    public void setBalance(double balance) {this.balance = balance;}
    

}
