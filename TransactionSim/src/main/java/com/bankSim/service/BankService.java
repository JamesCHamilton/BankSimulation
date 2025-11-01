package com.bankSim.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Async;
import jakarta.transaction.Transactional;


@Component
public class BankService {

    private final LoanProcessingService loanProcessingService;
    private final TransferService transferService;
    @Autowired
    public BankService(LoanProcessingService loanProcessingService, TransferService transferService){
        this.loanProcessingService = loanProcessingService;
        this.transferService = transferService;
    }

    @Async
    @Transactional
    public void processScheduledOperations(){

        System.out.println("Processing scheduled operations");

        try{
            processQueuedTransfers();
            applyInterestToLoans();
            System.out.println("✅ Bank cycle completed successfully.");
        }catch(Exception e){
            System.err.println("⚠️ Error during bank cycle: " + e.getMessage());
        }
    }

    private void processQueuedTransfers(){
        System.out.println("Processing queued transfers");
    
        transferService.processTransfer();
    }

    private void applyInterestToLoans(){
        System.out.println("Applying interest to loans");

        loanProcessingService.processLoanInterests();
    }

    



}
