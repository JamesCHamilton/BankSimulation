package com.bankSim.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bankSim.model.User;
import com.bankSim.dto.requests.UserCreationRequest;
import com.bankSim.model.Account;
import com.bankSim.repos.AccountRepository;
import com.bankSim.repos.LoanRepository;
import com.bankSim.model.Loan;
import com.bankSim.repos.UserRepository;
import com.bankSim.dto.responses.LoginInReponse;
import com.bankSim.dto.responses.UserCreationResponse;


import com.bankSim.config.JwtTokenProvider;
import com.bankSim.dto.requests.AccountCreationRequest;
import com.bankSim.dto.responses.AccountResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final AccountRepository accountRepository;
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    @Autowired 
    public UserService(AccountRepository accountRepository, LoanRepository loanRepository, UserRepository userRepository, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
        this.accountRepository = accountRepository;
        this.tokenProvider = tokenProvider;
    }
    
    public UserCreationResponse CreateUser(UserCreationRequest request){
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (request.getUserId() != null && userRepository.findById(request.getUserId()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        final String hashedPassword = encoder.encode(request.getPassword());

        User user = new User(
            request.getUserName(),
            request.getEmail(),
            hashedPassword,
            request.getFirstName(),
            request.getLastName()
            );

        userRepository.save(user);

        return new UserCreationResponse(user.getUserId(), user.getUserName(), user.getEmail(), "User created successfully");
    }

    public LoginInReponse loginUser(String email, String password){
        User user = userRepository.findByEmail(email);
        if(user == null){
            // Try to look up by username as a fallback
            user = userRepository.findAll().stream()
                    .filter(u -> u.getUserName().equalsIgnoreCase(email))
                    .findFirst()
                    .orElse(null);
            if (user == null) {
                return new LoginInReponse(null, "User not found", HttpStatus.NOT_FOUND);
            }
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        if(encoder.matches(password, user.getPassword())){
            String token = tokenProvider.generateToken(user.getEmail());
            return new LoginInReponse(token, "Login successful", HttpStatus.OK);
        } else {
            return new LoginInReponse(null, "Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    public void deleteUser(Long userId){
        userRepository.deleteById(userId);
    }

    @Cacheable(value = "accounts", key = "#user.id")
    public List<Account> getAllUserAccounts(User user){
        return accountRepository.findAllById(user.getAccountIds());
    }

    public Account getAccountById(User user, Long accountId){
        if(user.getAccountIds().contains(accountId)){
            return accountRepository.findById(accountId).orElse(null);
        }
        return null;
    }

    @Cacheable(value = "loans", key = "#user.id")
    public List<Loan> getAllUserLoans(User user){
        return loanRepository.findAllById(user.getLoanIds());
    }

    public Loan getLoanById(User user, Long LoanId){
        if(user.getAccountIds().contains(LoanId)){
            return loanRepository.findById(LoanId).orElse(null);
        }
        return null;
    }

    @Transactional
    @CacheEvict(value = "accounts", key = "#userId")
    public AccountResponse createAccount(Long userId, AccountCreationRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String accountNumber = request.getAccountNumber();
        if (accountNumber == null || accountNumber.isBlank()) {
            accountNumber = "ACT" + System.nanoTime();
        }
        String routingNumber = request.getRoutingNumber();
        if (routingNumber == null || routingNumber.isBlank()) {
            routingNumber = "RTN" + (int)(Math.random() * 100000000);
        }

        Account account = new Account(
            userId,
            accountNumber,
            user.getFirstName() + " " + user.getLastName(),
            routingNumber,
            request.getAccountType(),
            "Realistic Enterprise Bank"
        );

        if (request.getAmount() != null) {
            account.setBalance(request.getAmount());
        }

        account = accountRepository.save(account);

        user.getAccountIds().add(account.getId());
        userRepository.save(user);

        return new AccountResponse(
            account.getId(),
            account.getAccountNumber(),
            account.getAccountType(),
            account.getBalance().doubleValue(),
            user.getFirstName() + " " + user.getLastName()
        );
    }
}
