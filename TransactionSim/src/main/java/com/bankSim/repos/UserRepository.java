package com.bankSim.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bankSim.model.User;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}