package com.bankSim.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bankSim.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}