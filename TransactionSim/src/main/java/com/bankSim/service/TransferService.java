package com.bankSim.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.bankSim.repos.AccountRepository;
import com.bankSim.repos.TransferRepository;
import com.bankSim.queue.TransferQueue;
import com.bankSim.model.Account;
import com.bankSim.model.Transfer;
import com.bankSim.dto.requests.TransferRequest;
import com.bankSim.dto.responses.TransferResponse;
import com.bankSim.dto.tasks.TransferTask;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.bankSim.utils.Status;
import com.bankSim.utils.TransferTypes;


import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.UUID;



public class TransferService {

    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;    

    @Autowired
    private TransferQueue transferQueue;

    @Autowired 
    public TransferService(TransferRepository transferRepository, AccountRepository accountRepository) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
    }

    public TransferResponse initializeTransfer(TransferRequest request){

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return new TransferResponse(Status.FAILED, request.getTransferId(), null, null , "Transfer amount must be greater than zero.");
        }

        Transfer transfer = new Transfer(
            request.getFromAccountId(),
            request.getToAccountId(),
            request.getAmount(),
            request.getTransferType()
        );

        
        transferRepository.save(transfer);

        // add to queue
        try {
            transferQueue.enqueue(new TransferTask(transfer.getId(), transfer.getFromAccountId(), transfer.getToAccountId(), transfer.getAmount()));
            return new TransferResponse(Status.QUEUED, transfer.getId(), null, request.getTransferType() , "Transfer was queued");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new TransferResponse(Status.FAILED, transfer.getId(), null, request.getTransferType() ,"Transfer interrupted");
        }
    }

    @Transactional
    @Scheduled(fixedDelay = 3000)
    public void processTransfer(){
        try{
            TransferTask task = transferQueue.dequeue();
            if(task != null){
                processTransferTask(task);
            }
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
            System.err.println("Transfer processing interrupted: " + e.getMessage());
            return;
        }
    }

    public void processTransferTask(TransferTask task){
        Optional<Account> fromOpt = accountRepository.findById(task.getFromAccountId());
        Optional<Account> toOpt = accountRepository.findById(task.getToAccountId());


    }


}
