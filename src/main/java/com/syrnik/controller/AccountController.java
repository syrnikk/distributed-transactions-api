package com.syrnik.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.syrnik.dto.AccountDTO;
import com.syrnik.service.AccountService;
import com.syrnik.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;

    @GetMapping("/account")
    public ResponseEntity<List<AccountDTO>> getAccounts(@RequestParam Integer userId) {
        log.info("REST Get accounts by user id: {}", userId);
        List<AccountDTO> accounts = accountService.findAccountsByUserId(userId);
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/account")
    public ResponseEntity<?> createAccount(@RequestParam Integer userId, @RequestBody AccountDTO dto) {
        log.info("REST Create account: {}", dto);
        accountService.createAccount(userId, dto);
        return ResponseEntity.ok().build();
    }
}
