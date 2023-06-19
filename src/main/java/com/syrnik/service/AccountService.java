package com.syrnik.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.syrnik.dto.AccountDTO;
import com.syrnik.exception.AccountNotFoundException;
import com.syrnik.exception.EntityNotFoundException;
import com.syrnik.model.central.Account;
import com.syrnik.model.central.User;
import com.syrnik.repository.central.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserService userService;

    public void createAccount(Integer userId, AccountDTO dto) {
        User user = userService.findById(userId).orElseThrow(EntityNotFoundException::new);
        Account account = new Account();
        account.setUser(user);
        account.setAccountNumber(dto.getAccountNumber());
        account.setBalance(dto.getBalance());
        accountRepository.save(account);
    }

    public List<AccountDTO> findAccountsByUserId(Integer userId) {
        List<Account> accounts = accountRepository.findAllByUserId(userId);
        return accounts
              .stream()
              .map(account -> new AccountDTO(account.getAccountNumber(), account.getBalance()))
              .toList();
    }

    public boolean hasAccessToAccount(Integer userId, String accountNumber) {
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
        Account account = optionalAccount.orElseThrow(AccountNotFoundException::new);
        return account.getUser().getId() == userId;
    }
}
