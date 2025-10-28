package com.bankSim.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.bankSim.repos.LoanRepository;
import com.bankSim.repos.AccountRepository;
import com.bankSim.repos.UserRepository;
import com.bankSim.repos.TransferRepository;
import com.bankSim.dto.requests.LoanPaymentRequest;


import java.math.BigDecimal;
import java.util.Optional;

import com.bankSim.model.Loan;

public class LoanService {
    
    @Autowired
    private final LoanRepository loanRepository;
    @Autowired
    private final AccountRepository accountRepository; 
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final TransferRepository transferRepository;

    @Autowired 
    public LoanService(LoanRepository loanRepository, AccountRepository accountRepository, UserRepository userRepository, TransferRepository transferRepository) {
        this.loanRepository = loanRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transferRepository = transferRepository;
    }

    public void loanPayment(LoanPaymentRequest request, String loanId){
        Optional<Loan> loan = loanRepository.findById(request.getLoanId());
        if(loan.isEmpty()){
            throw new IllegalArgumentException("Loan not found");
        }

        

        
        
        
    }

    

    
}
