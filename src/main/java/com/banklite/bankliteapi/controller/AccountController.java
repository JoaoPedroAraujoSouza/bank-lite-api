package com.banklite.bankliteapi.controller;

import com.banklite.bankliteapi.dto.account.AccountRequest;
import com.banklite.bankliteapi.dto.account.AccountResponse;
import com.banklite.bankliteapi.dto.account.UpdateAccountRequest;
import com.banklite.bankliteapi.dto.transaction.TransactionResponse;
import com.banklite.bankliteapi.service.AccountService;
import com.banklite.bankliteapi.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")

public class AccountController {

    AccountService accountService;
    TransactionService transactionService;

    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest accountRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(accountRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.findAccountById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(@PathVariable Long id, @RequestBody UpdateAccountRequest accountRequest) {
        return ResponseEntity.ok(accountService.updateAccount(id, accountRequest));
    }

    @GetMapping("/{id}/statement")
    public ResponseEntity<List<TransactionResponse>> getAccountStatement(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getStatement(id));
    }
}
