package com.bankSim.config;

import com.bankSim.dto.requests.AccountCreationRequest;
import com.bankSim.dto.requests.UserCreationRequest;
import com.bankSim.dto.requests.LoanCreationRequest;
import com.bankSim.service.LoanService;
import com.bankSim.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;

@Component
@Profile("dev")
public class MockDataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MockDataInitializer.class);
    
    private final UserService userService;
    private final LoanService loanService;

    public MockDataInitializer(UserService userService, LoanService loanService) {
        this.userService = userService;
        this.loanService = loanService;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Initializing mock bank data for DEV profile...");

        try {
            // 1. Create a mock user
            UserCreationRequest userRequest = new UserCreationRequest(
                    1L,
                    "John",
                    "Doe",
                    "john.doe@bank.com",
                    "johndoe",
                    "securepassword123",
                    new ArrayList<>(),
                    new ArrayList<>()
            );
            userService.CreateUser(userRequest);
            logger.info("Created mock user: john.doe@bank.com (password: securepassword123)");

            // 2. Create user accounts
            AccountCreationRequest checkingRequest = new AccountCreationRequest(
                    null, "CHECKING", "RTN123456", "ACT-JOHN-001"
            );
            
            userService.createAccount(1L, checkingRequest);
            logger.info("Created checking account for John Doe");

            AccountCreationRequest savingsRequest = new AccountCreationRequest(
                    null, "SAVINGS", "RTN123456", "ACT-JOHN-002"
            );
            userService.createAccount(1L, savingsRequest);
            logger.info("Created savings account for John Doe");

            // 3. Create user loan
            LoanCreationRequest loanRequest = new LoanCreationRequest(
                    null, BigDecimal.valueOf(5000.00), 24, 5.5, "PERSONAL", "APPROVED", "MONTHLY"
            );
            loanService.createLoan(1L, loanRequest);
            logger.info("Created mock loan for John Doe");

            logger.info("✅ DEV mock data initialized successfully!");
        } catch (Exception e) {
            logger.error("Failed to initialize mock data: {}", e.getMessage());
        }
    }
}
