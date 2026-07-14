package com.bankSim.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.bankSim.dto.tasks.TransferTask;
import com.bankSim.exceptions.ResourceNotFoundException;
import com.bankSim.exceptions.InsufficientFundsException;
import com.bankSim.queue.TransferQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;

@Service
public class TransferQueueWorker {
    
    private static final Logger logger = LoggerFactory.getLogger(TransferQueueWorker.class);

    private final TransferQueue transferQueue;
    private final TransferService transferService;
    private final ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    public TransferQueueWorker(TransferQueue transferQueue, TransferService transferService, @Qualifier("taskExecutor") ThreadPoolTaskExecutor taskExecutor){
        this.transferQueue = transferQueue;
        this.transferService = transferService;
        this.taskExecutor = taskExecutor;
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
                    // Monitor pool size & queue depth
                    logger.info("ThreadPool Monitoring - Active Threads: {}, Queue size: {}",
                            taskExecutor.getActiveCount(), taskExecutor.getQueueSize());

                    taskExecutor.execute(() -> {
                        try {
                            transferService.processTransferTask(task);
                        } catch (ResourceNotFoundException | InsufficientFundsException e) {
                            logger.error("Error processing transfer task ID {}: {}", task.getTransferId(), e.getMessage());
                        }
                    });
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Transfer queue worker interrupted: {}", e.getMessage());
                return;
            } catch(Exception e){
                logger.error("Error in transfer queue loop: {}", e.getMessage());
                return;
            }
        }
    }
}
