package com.bankSim.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import com.bankSim.dto.tasks.TransferTask;
import com.bankSim.queue.TransferQueue;


public class TransferQueueWorker {
    
    @Autowired
    private final TransferQueue transferQueue;

    @Autowired
    private final TransferService transferService;

    @Autowired
    public TransferQueueWorker(TransferQueue transferQueue, TransferService transferService){
        this.transferQueue = transferQueue;
        this.transferService = transferService;
    }

     @EventListener(ApplicationReadyEvent.class)
     public void startWorker(){
        int threadcount = 3;

        for (int i = 0; i < threadcount; i++){
            Thread workerThread = new Thread(() -> {
                while(true){
                    try{
                        TransferTask task = transferQueue.dequeue();
                        transferService.processTransferTask(task);
                    }catch(InterruptedException e){
                        Thread.currentThread().interrupt();
                        break;
                    }catch(Exception e){
                        // Log exceptnion
                        System.err.println("Error processing transfer task: " + e.getMessage());
                        Thread.currentThread().interrupt();
                    }
                }
            }, "transfer-worker-" + i);
            workerThread.setDaemon(true);
            workerThread.start();
        }
     }



   
}
