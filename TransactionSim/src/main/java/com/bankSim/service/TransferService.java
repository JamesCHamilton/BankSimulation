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

        try{

            transfer.setUpdatedAt(LocalDateTime.now());
            transferRepository.save(transfer);
            
        }catch(Exception e){
            return new TransferResponse(Status.FAILED, null, null, request.getTransferType() , "Error initializing transfer: " + e.getMessage());
        }

        // add to queue
        try {
            transferQueue.enqueue(new TransferTask(
                transfer.getId(), 
                transfer.getFromAccountId(), 
                transfer.getToAccountId(), 
                transfer.getAmount(), 
                transfer.getTransferType()
                ));
            
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
        Optional<Account> fromAccount = accountRepository.findById(task.getFromAccountId());
        Optional<Account> toAccount = accountRepository.findById(task.getToAccountId());
        Optional<Transfer> transferOpt = transferRepository.findById(task.getTransferId());

        if(!fromAccount.isPresent() || !toAccount.isPresent() || !transferOpt.isPresent()){
            transferOpt.ifPresent(transfer -> {
                transfer.setStatus(Status.FAILED);
                transfer.setMessage("One or more accounts not found.");
                transferRepository.save(transfer);
            });
            return;
        }

        Account fromAcc = fromAccount.get();
        Account toAcc = toAccount.get();
        Transfer transfer = transferOpt.get();

        try{
            switch (task.getTransferType()) {
                case TransferTypes.INPERSONWITHDRAW:
                    if (fromAcc.getBalance().compareTo(task.getAmount()) < 0) {
                        transfer.setStatus(Status.FAILED);
                        transfer.setMessage("Insufficient funds.");
                        transferRepository.save(transfer);
                        return;
                    }else{
                        fromAcc.setBalance(fromAcc.getBalance().subtract(task.getAmount()));
                        accountRepository.save(fromAcc);

                        transfer.setStatus(Status.SUCCESS);
                        transfer.setMessage("Withdrawal successful.");
                        transferRepository.save(transfer);
                    }
                    break;
                
                case TransferTypes.INPERSONDEPOSIT:
                    toAcc.setBalance(toAcc.getBalance().add(task.getAmount()));
                    accountRepository.save(toAcc);

                    transfer.setStatus(Status.SUCCESS);
                    transfer.setMessage("Deposit successful.");
                    transferRepository.save(transfer);
                    break;

                case TransferTypes.ACCOUNTtoACCOUNTTRANSFER:
                    if (fromAcc.getBalance().compareTo(task.getAmount()) < 0) {
                        transfer.setStatus(Status.FAILED);
                        transfer.setMessage("Insufficient funds for account-to-account transfer.");
                        transferRepository.save(transfer);
                        return;
                    }else{
                        fromAcc.setBalance(fromAcc.getBalance().subtract(task.getAmount()));
                        toAcc.setBalance(toAcc.getBalance().add(task.getAmount()));
                        accountRepository.save(fromAcc);
                        accountRepository.save(toAcc);
                        transfer.setStatus(Status.SUCCESS);
                        transfer.setMessage("Account-to-account transfer successful.");
                        transferRepository.save(transfer);
                    }
                    break;
                default:
                    break;
            }
        }catch(Exception e){
            transfer.setStatus(Status.FAILED);
            transfer.setMessage("Error processing transfer: " + e.getMessage());
            transferRepository.save(transfer);
        }


    }


}
