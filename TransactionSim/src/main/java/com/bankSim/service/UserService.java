package com.bankSim.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bankSim.model.User;
import com.bankSim.dto.requests.UserCreationRequest;
import com.bankSim.model.Account;
import com.bankSim.repos.AccountRepository;
import com.bankSim.repos.LoanRepository;
import com.bankSim.model.Loan;
import com.bankSim.repos.UserRepository;
import com.bankSim.dto.responses.LoginInReponse;
import com.bankSim.dto.responses.UserCreationResponse;

public class UserService {

    @Autowired
    private final AccountRepository accountRepository;

    @Autowired
    private final LoanRepository loanRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired 
    public UserService(AccountRepository accountRepository, LoanRepository loanRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
        this.accountRepository = accountRepository;
    }
    
    public UserCreationResponse CreateUser(UserCreationRequest request){
        
        if (userRepository.findById(request.getUserId()).isPresent()) {
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
            return new LoginInReponse(null, "User not found", HttpStatus.NOT_FOUND);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        if(encoder.matches(password, user.getPassword())){
            //need to implement JWT token 
            return new LoginInReponse(null, "Login successful", HttpStatus.OK);
        } else {
            return new LoginInReponse(null, "Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    public void deleteUser(Long userId){
        userRepository.deleteById(userId);
    }

    public List<Account> getAllUserAccounts(User user){
        return accountRepository.findAllById(user.getAccountIds());
    }

    public Account getAccountById(User user, Long accountId){
        if(user.getAccountIds().contains(accountId)){
            return accountRepository.findById(accountId).orElse(null);
        }
        return null;
    }

    public List<Loan> getAllUserLoans(User user){
        return loanRepository.findAllById(user.getLoanIds());
    }

    public Loan getLoanById(User user, Long LoanId){
        if(user.getAccountIds().contains(LoanId)){
            return loanRepository.findById(LoanId).orElse(null);
        }
        return null;
    }

}
