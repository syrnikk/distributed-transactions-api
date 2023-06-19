package com.syrnik;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.syrnik.dto.TransactionRequestDTO;
import com.syrnik.exception.AccountNotFoundException;
import com.syrnik.exception.InsufficientBalanceException;
import com.syrnik.model.branch.Transaction;
import com.syrnik.model.central.Account;
import com.syrnik.repository.branch.TransactionRepository;
import com.syrnik.repository.central.AccountRepository;
import com.syrnik.service.TransactionService;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void testPerformTransaction() throws InsufficientBalanceException, AccountNotFoundException {
        // Mock sender and recipient accounts
        Account senderAccount = new Account();
        senderAccount.setAccountNumber("sender-account-number");
        senderAccount.setBalance(new BigDecimal("1000.00"));

        Account recipientAccount = new Account();
        recipientAccount.setAccountNumber("recipient-account-number");
        recipientAccount.setBalance(new BigDecimal("500.00"));

        when(accountRepository.findByAccountNumber("sender-account-number"))
              .thenReturn(Optional.of(senderAccount));
        when(accountRepository.findByAccountNumber("recipient-account-number"))
              .thenReturn(Optional.of(recipientAccount));

        // Perform the transaction
        TransactionRequestDTO transactionRequest = new TransactionRequestDTO();
        transactionRequest.setSenderAccountNumber("sender-account-number");
        transactionRequest.setRecipientAccountNumber("recipient-account-number");
        transactionRequest.setAmount(new BigDecimal("200.00"));
        transactionRequest.setDescription("Payment");

        transactionService.performTransaction(transactionRequest);

        // Verify account balances have been updated
        verify(accountRepository, times(1)).save(senderAccount);
        verify(accountRepository, times(1)).save(recipientAccount);

        // Verify a new transaction record has been created
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
}
