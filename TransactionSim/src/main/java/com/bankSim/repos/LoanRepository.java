package com.bankSim.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bankSim.model.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}