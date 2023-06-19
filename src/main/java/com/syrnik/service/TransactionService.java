package com.syrnik.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.syrnik.dto.TransactionRequestDTO;
import com.syrnik.exception.AccountNotFoundException;
import com.syrnik.exception.InsufficientBalanceException;
import com.syrnik.model.branch.Transaction;
import com.syrnik.model.central.Account;
import com.syrnik.repository.branch.TransactionRepository;
import com.syrnik.repository.central.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    public List<Transaction> getUserTransactions(Integer userId) {
        List<Account> accounts = accountRepository.findAllByUserId(userId);
        List<String> accountNumbers = accounts.stream().map(Account::getAccountNumber).toList();
        return transactionRepository.findUserTransactions(accountNumbers);
    }

    @Transactional("chainedTransactionManager")
    public void performTransaction(TransactionRequestDTO transactionRequest)
          throws InsufficientBalanceException, AccountNotFoundException {
        // Get the sender and recipient accounts
        Account senderAccount = accountRepository.findByAccountNumber(transactionRequest.getSenderAccountNumber())
                                                 .orElseThrow(AccountNotFoundException::new);
        Account recipientAccount = accountRepository.findByAccountNumber(transactionRequest.getRecipientAccountNumber())
                                                    .orElseThrow(AccountNotFoundException::new);

        // Validate sender has enough balance
        if (senderAccount.getBalance().compareTo(transactionRequest.getAmount()) < 0) {
            throw new InsufficientBalanceException();
        }

        // Update account balances
        senderAccount.setBalance(senderAccount.getBalance().subtract(transactionRequest.getAmount()));
        recipientAccount.setBalance(recipientAccount.getBalance().add(transactionRequest.getAmount()));

        // Save the updated account balances
        accountRepository.save(senderAccount);
        accountRepository.save(recipientAccount);

        // Create a new transaction record
        Transaction transaction = new Transaction();
        transaction.setSenderAccountNumber(senderAccount.getAccountNumber());
        transaction.setRecipientAccountNumber(recipientAccount.getAccountNumber());
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDescription(transactionRequest.getDescription());
        transaction.setTransactionDate(LocalDateTime.now());

        // Save the transaction record
        transactionRepository.save(transaction);
    }
}
