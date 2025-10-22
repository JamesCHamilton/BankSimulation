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
    private double loanAmount;
    private double interestRate;
    private int termMonths;
    private double monthlyPayment;

    protected Loan() {}

    public Loan(String loanId, String userId, double loanAmount, double interestRate, int termMonths) {
        this.loanId = loanId;
        this.userId = userId;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.termMonths = termMonths;
        this.monthlyPayment = calculateMonthlyPayment();
    }

    public String getLoanId() {return loanId;}
    public String getUserId() {return userId;}
    public double getLoanAmount() {return loanAmount;}
    public double getInterestRate() {return interestRate;}
    public int getTermMonths() {return termMonths;}
    public double getMonthlyPayment() {return monthlyPayment;}

    private double calculateMonthlyPayment() {
        double monthlyInterestRate = interestRate / 12 / 100;
        return (loanAmount * monthlyInterestRate) / (1 - Math.pow(1 + monthlyInterestRate, -termMonths));
    }

}
