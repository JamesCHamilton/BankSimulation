package com.bankSim.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id 
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_accounts", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "account_id")
    private List<Long> accountIds;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_loans", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "loan_id")
    private List<Long> loanIds;

    protected User() {}

    public User(String userName, String email, String password, String firstName, String lastName) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accountIds = new ArrayList<>();
        this.loanIds = new ArrayList<>();

    }

    public Long getUserId() {return id;}
    public String getUserName() {return userName;}
    public String getPassword() {return password;}
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public List<Long> getAccountIds() {return accountIds;}
    public List<Long> getLoanIds() {return loanIds;}
    public String getEmail() {return email;}
    public Long getId() {return id;}
    



}
