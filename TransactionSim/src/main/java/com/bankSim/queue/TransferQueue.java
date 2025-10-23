package com.bankSim.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.stereotype.Component;

import com.bankSim.dto.tasks.TransferTask;

@Component
public class TransferQueue {
    private final BlockingQueue<TransferTask> queue = new LinkedBlockingQueue<>();


    public void enqueue(TransferTask request) throws InterruptedException {
        queue.put(request);
    }

    public TransferTask dequeue() throws InterruptedException{
        return queue.take();
    }

    public boolean isEmpty(){
        return queue.isEmpty();
    }


}

