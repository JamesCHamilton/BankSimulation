package com.bankSim.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.bankSim.utils.Status;

@Entity

public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

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


    public Transfer() {
        this.id = UUID.randomUUID().toString();
        this.status = Status.QUEUED;
        this.createdAt = LocalDateTime.now();
    }

    public Transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        this();
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
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
}


