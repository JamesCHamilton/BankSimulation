package com.bankSim.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.bankSim.model.User;
import com.bankSim.model.Account;
import com.bankSim.repos.AccountRepository;
import com.bankSim.repos.LoanRepository;
import com.bankSim.model.Loan;
import com.bankSim.repos.UserRepository;

public class UserService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    
    public User CreateUser(String firstName, String lastName, String email, String userName, String password, String userId){
        
    //     if (userRepository.findByEmail(email).isPresent()) {
    //         throw new IllegalArgumentException("Username already exists");
    //     }
    //     return new User(userName, password, firstName, lastName);
            return null;
    }

    public List<Account> getAllUserAccounts(User user){
        return accountRepository.findAllById(user.getAccountIds());
    }

    public Account getAccountById(User user, Long accountId){
        if(user.getAccountIds().contains(accountId)){
            return accountRepository.findById(accountId).orElse(null);
        }
        return null;
    }

    public List<Loan> getAllUserLoans(User user){
        return loanRepository.findAllById(user.getLoanIds());
    }

    public Loan getLoanById(User user, Long LoanId){
        if(user.getAccountIds().contains(LoanId)){
            return loanRepository.findById(LoanId).orElse(null);
        }
        return null;
    }

}
