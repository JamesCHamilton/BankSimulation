package com.bankSim.service;

import org.springframework.beans.factory.annotation.Autowired;


import com.bankSim.dto.tasks.TransferTask;
import com.bankSim.exceptions.ResourceNotFoundException;
import com.bankSim.exceptions.InsufficientFundsException;
import com.bankSim.queue.TransferQueue;

import jakarta.annotation.PostConstruct;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TransferQueueWorker {
    
    @Autowired
    private final TransferQueue transferQueue;

    @Autowired
    private final TransferService transferService;
    private final ExecutorService executorService;

    @Autowired
    public TransferQueueWorker(TransferQueue transferQueue, TransferService transferService, ExecutorService executorService){
        this.transferQueue = transferQueue;
        this.transferService = transferService;
        this.executorService = Executors.newFixedThreadPool(3);
    }

    @PostConstruct
    public void startWorking(){

        Thread workerThread = new Thread(this::processQueueLoop, "transfer-queue-worker");
        workerThread.setDaemon(true);
        workerThread.start();
    }

    private void processQueueLoop(){
        while(true){
            try {
                TransferTask task = transferQueue.dequeue();
                if(task != null){
                    executorService.execute(() -> {
                        try {
                            transferService.processTransferTask(task);
                        } catch (ResourceNotFoundException | InsufficientFundsException e) {
                            System.err.println("Error processing transfer: " + e.getMessage());
                        }
                    });
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Transfer queue worker interrupted: " + e.getMessage());
                return;
            }catch(Exception e){
                System.err.println("Error processing transfer queue" + e.getMessage());
                return;
            }
        }
    }


   
}
