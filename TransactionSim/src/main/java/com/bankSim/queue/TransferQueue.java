package com.bankSim.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.stereotype.Component;

import com.bankSim.dto.requests.TransferRequest;

@Component
public class TransferQueue {
    private final BlockingQueue<TransferRequest> queue = new LinkedBlockingQueue<>();


    public void enqueue(TransferRequest request) throws InterruptedException {
        queue.put(request);
    }

    public TransferRequest dequeue() throws InterruptedException{
        return queue.take();
    }

    public boolean isEmpty(){
        return queue.isEmpty();
    }


}

