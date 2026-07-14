package com.bankSim.controller;

import com.bankSim.dto.requests.AccountCreationRequest;
import com.bankSim.dto.requests.LoanCreationRequest;
import com.bankSim.dto.requests.LoanPaymentRequest;
import com.bankSim.dto.requests.TransferRequest;
import com.bankSim.dto.responses.AccountResponse;
import com.bankSim.dto.responses.LoanCreationReponse;
import com.bankSim.dto.responses.LoanPaymentReponse;
import com.bankSim.dto.responses.TransferProjection;
import com.bankSim.dto.responses.TransferResponse;
import com.bankSim.model.Account;
import com.bankSim.model.Loan;
import com.bankSim.model.User;
import com.bankSim.repos.UserRepository;
import com.bankSim.service.LoanService;
import com.bankSim.service.TransferService;
import com.bankSim.service.UserService;
import com.bankSim.exceptions.ResourceNotFoundException;
import com.bankSim.exceptions.UnauthorizedAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AccounterController {

    private final UserService userService;
    private final LoanService loanService;
    private final TransferService transferService;
    private final UserRepository userRepository;

    public AccounterController(UserService userService, LoanService loanService, TransferService transferService, UserRepository userRepository) {
        this.userService = userService;
        this.loanService = loanService;
        this.transferService = transferService;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername());
        if (user == null) {
            throw new IllegalArgumentException("User not found in system");
        }
        return user;
    }

    @PostMapping("/accounts")
    public ResponseEntity<AccountResponse> createAccount(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AccountCreationRequest request) {
        User user = getAuthenticatedUser(userDetails);
        AccountResponse response = userService.createAccount(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccounts(@AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        List<Account> accounts = userService.getAllUserAccounts(user);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Account> getAccountById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        User user = getAuthenticatedUser(userDetails);
        Account account = userService.getAccountById(user, id);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody TransferRequest request) throws UnauthorizedAccessException, ResourceNotFoundException {
        User user = getAuthenticatedUser(userDetails);
        TransferResponse response = transferService.initializeTransfer(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/loans")
    public ResponseEntity<LoanCreationReponse> applyForLoan(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody LoanCreationRequest request) {
        User user = getAuthenticatedUser(userDetails);
        LoanCreationReponse response = loanService.createLoan(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/loans/payment")
    public ResponseEntity<LoanPaymentReponse> payLoan(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody LoanPaymentRequest request) throws UnauthorizedAccessException, ResourceNotFoundException {
        User user = getAuthenticatedUser(userDetails);
        LoanPaymentReponse response = loanService.loanPayment(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/loans")
    public ResponseEntity<List<Loan>> getLoans(@AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        List<Loan> loans = userService.getAllUserLoans(user);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransferProjection>> getTransactions(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        User user = getAuthenticatedUser(userDetails);
        LocalDateTime fromDateTime = from.atStartOfDay();
        LocalDateTime toDateTime = to.atTime(23, 59, 59);
        List<TransferProjection> transactions = transferService.getTransactionsForUser(user.getId(), fromDateTime, toDateTime);
        return ResponseEntity.ok(transactions);
    }
}
