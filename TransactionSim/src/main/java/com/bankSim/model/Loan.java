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

    private double princaipalAmount;
    private double interestRate;
    private int termMonths;
    private double balance;

    protected Loan() {}

    public Loan (double princaipalAmount, double balance, double interestRate, int termMonths) {
        this.balance = balance;
        this.interestRate = interestRate;
        this.termMonths = termMonths;
    }

    public Long getLoanId() {return id;}
    public double getInterestRate() {return interestRate;}
    public int getTermMonths() {return termMonths;}
    public double getBalance() {return balance;}
    public double getLoanAmount() {return princaipalAmount;}
    public void setBalance(double balance) {this.balance = balance;}
    

}
