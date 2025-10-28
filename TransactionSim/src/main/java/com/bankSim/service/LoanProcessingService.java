package com.bankSim.service;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bankSim.repos.LoanRepository;
import com.bankSim.model.Loan;

import jakarta.transaction.Transactional;

import java.util.List;
import java.time.LocalDate;

@Service
@EnableScheduling
public class LoanProcessingService {
    private final LoanRepository loanRepository;

    public LoanProcessingService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Scheduled(cron = "0 0 0 1 * *")// Runs once monthly 
    @Transactional
    public void processLoanInterests(){
        List<Loan> loans = loanRepository.findAll();

        for(Loan loan : loans){
            double monethlyInterest = loan.getLoanAmount() * (loan.getInterestRate() / 100) / 12;
            loan.setBalance(loan.getBalance() + monethlyInterest);
        }

        loanRepository.saveAll(loans);
        System.out.println("Loan interests processed " + loans.size() + "loans on " + LocalDate.now());
    }
}

