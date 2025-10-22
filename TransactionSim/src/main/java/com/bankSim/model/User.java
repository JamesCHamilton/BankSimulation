package com.bankSim.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id 
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private List<Long> accountIds;
    private List<Long> loanIds;

    protected User() {}

    User(String userId, String userName, String password, String firstName, String lastName, List<Long> accountIds, List<Long> loanIds) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountIds = new ArrayList<>();
        this.loanIds = new ArrayList<>();

    }

    public String getUserId() {return userId;}
    public String getUserName() {return userName;}
    public String getPassword() {return password;}
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public List<Long> getAccountIds() {return accountIds;}
    public List<Long> getLoanIds() {return loanIds;}



}
