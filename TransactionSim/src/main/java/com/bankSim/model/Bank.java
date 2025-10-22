package com.bankSim.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "banks")
public abstract class Bank {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String bankName;
    private String bankCode;
    private String bankAddress;
    private String bankPhoneNumber;
    private String policyKey;
    private String swiftCode;

    protected Bank() {}

    Bank(String bankName, String bankCode, String bankAddress, String bankPhoneNumber, String swiftCode, String policyKey) {
        this.bankName = bankName;
        this.bankCode = bankCode;
        this.bankAddress = bankAddress;
        this.bankPhoneNumber = bankPhoneNumber;
        this.swiftCode = swiftCode;
        this.policyKey = policyKey;
    }

    public String getBankName() {return bankName;}
    public String getBankCode() {return bankCode;}
    public String getBankAddress() {return bankAddress;}
    public String getBankPhoneNumber() {return bankPhoneNumber;}
    public String getPolicyKey() {return policyKey;}
    public String getSwiftCode() {return swiftCode;}

}

