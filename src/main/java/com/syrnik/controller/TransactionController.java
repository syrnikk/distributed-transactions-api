package com.syrnik.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.syrnik.dto.TransactionDTO;
import com.syrnik.dto.TransactionRequestDTO;
import com.syrnik.exception.AccountNotFoundException;
import com.syrnik.exception.InsufficientBalanceException;
import com.syrnik.model.branch.Transaction;
import com.syrnik.security.UserDetailsImpl;
import com.syrnik.service.AccountService;
import com.syrnik.service.TransactionService;
import com.syrnik.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final AccountService accountService;

    @GetMapping("/transaction")
    public ResponseEntity<List<TransactionDTO>> getTransactions(@RequestParam Integer userId) {
        List<Transaction> transactions = transactionService.getUserTransactions(userId);
        return ResponseEntity.ok(transactions
              .stream()
              .map(transaction -> TransactionDTO
                    .builder()
                    .id(transaction.getId())
                    .senderAccountNumber(transaction.getSenderAccountNumber())
                    .recipientAccountNumber(transaction.getRecipientAccountNumber())
                    .amount(transaction.getAmount())
                    .description(transaction.getDescription())
                    .transactionDate(transaction.getTransactionDate())
                    .build())
              .toList());
    }

    @PostMapping("/transaction")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionRequestDTO transactionRequest) {
        UserDetailsImpl loggedInUser = userService.getLoggedInUser();
        if(!accountService.hasAccessToAccount(loggedInUser.getId(), transactionRequest.getSenderAccountNumber())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            transactionService.performTransaction(transactionRequest);
            return ResponseEntity.ok().build();
        } catch(InsufficientBalanceException e) {
            return ResponseEntity.badRequest().body("Insufficient balance");
        } catch(AccountNotFoundException e) {
            return ResponseEntity.badRequest().body("Invalid account number");
        }
    }
}
