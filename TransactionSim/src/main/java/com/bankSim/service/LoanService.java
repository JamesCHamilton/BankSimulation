package com.bankSim.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.bankSim.repos.LoanRepository;
import com.bankSim.repos.AccountRepository;
import com.bankSim.repos.UserRepository;
import com.bankSim.utils.Status;

import jakarta.transaction.Transactional;

import com.bankSim.repos.TransferRepository;
import com.bankSim.dto.requests.LoanPaymentRequest;
import com.bankSim.dto.responses.LoanPaymentReponse;
import com.bankSim.exceptions.ResourceNotFoundException;
import com.bankSim.exceptions.UnauthorizedAccessException;

import java.time.LocalDateTime;
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

    @Transactional
    public LoanPaymentReponse loanPayment(Long userId, LoanPaymentRequest request) throws UnauthorizedAccessException, ResourceNotFoundException{
        Optional<Loan> loan = loanRepository.findById(request.getLoanId());
        if(loan.isEmpty()){
            throw new ResourceNotFoundException("Loan not found");
        }

        double paymentAmount = request.getPaymentAmount();
        if(paymentAmount <= 0){
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }

        Loan existingLoan = loan.get();
        
        if(!existingLoan.getUserId().equals(userId)){
            throw new UnauthorizedAccessException("User does not own the loan");
        }
        
        double newBalance = existingLoan.getBalance() - paymentAmount;
        if(newBalance < 0){
            throw new IllegalArgumentException("Payment amount exceeds outstanding balance" + existingLoan.getBalance());
        }
        
        else if(newBalance == 0){
            deleteLoan(existingLoan.getLoanId());
            return new LoanPaymentReponse(Status.SUCCESS, existingLoan.getLoanId(), 0.0, "Loan fully paid and closed");
        }

        existingLoan.setBalance(newBalance);
        existingLoan.setLastPaidAt(LocalDateTime.now());

        loanRepository.save(existingLoan);

        return new LoanPaymentReponse(Status.SUCCESS, existingLoan.getLoanId(), newBalance, "Loan payment successful");
    }

    private void deleteLoan(Long loanId){
        loanRepository.deleteById(loanId);
    }
    
}
