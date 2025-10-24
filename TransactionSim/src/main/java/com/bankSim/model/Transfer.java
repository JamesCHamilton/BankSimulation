package com.bankSim.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bankSim.utils.Status;


@Entity

public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long fromAccountId;

    @Column(nullable = false)
    private Long toAccountId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String status; 

    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String transferType;

    public Transfer() {
        this.status = Status.QUEUED;
        this.createdAt = LocalDateTime.now();
    }

    public Transfer(Long fromAccountId, Long toAccountId, BigDecimal amount, String transferType) {
        this();
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.transferType = transferType;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {return id;}
    public BigDecimal getAmount() {return amount;}
    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
    public String getMessage() {return message;}
    public void setMessage(String message) {this.message = message;}
    public Long getFromAccountId() {return fromAccountId;}
    public Long getToAccountId() {return toAccountId;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public LocalDateTime getUpdatedAt() {return updatedAt;}
    public void setUpdatedAt(LocalDateTime updatedAt) {this.updatedAt = updatedAt;} 
    public String getTransferType() {return transferType;}

}


