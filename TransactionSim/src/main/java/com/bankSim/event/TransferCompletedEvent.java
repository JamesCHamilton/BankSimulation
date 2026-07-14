package com.bankSim.event;

import com.bankSim.model.Transfer;
import org.springframework.context.ApplicationEvent;

public class TransferCompletedEvent extends ApplicationEvent {
    private final Transfer transfer;

    public TransferCompletedEvent(Object source, Transfer transfer) {
        super(source);
        this.transfer = transfer;
    }

    public Transfer getTransfer() {
        return transfer;
    }
}
