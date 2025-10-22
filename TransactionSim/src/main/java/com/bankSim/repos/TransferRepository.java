package com.bankSim.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bankSim.model.Transfer;


public interface TransferRepository extends JpaRepository<Transfer, Long> {
}