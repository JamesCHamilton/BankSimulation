package com.bankSim.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "analytics")
public class Analytics {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private LocalDate date;
    private long totalTransfersCount;
    private BigDecimal totalAmountTransferred;

    protected Analytics() {}

    public Analytics(LocalDate date, long totalTransfersCount, BigDecimal totalAmountTransferred) {
        this.date = date;
        this.totalTransfersCount = totalTransfersCount;
        this.totalAmountTransferred = totalAmountTransferred;
    }

    public Long getId() { return id; }
    public LocalDate getDate() { return date; }
    public long getTotalTransfersCount() { return totalTransfersCount; }
    public void setTotalTransfersCount(long totalTransfersCount) { this.totalTransfersCount = totalTransfersCount; }
    public BigDecimal getTotalAmountTransferred() { return totalAmountTransferred; }
    public void setTotalAmountTransferred(BigDecimal totalAmountTransferred) { this.totalAmountTransferred = totalAmountTransferred; }
}
