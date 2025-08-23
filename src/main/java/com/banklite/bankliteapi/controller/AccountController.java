package com.banklite.bankliteapi.controller;

import com.banklite.bankliteapi.dto.account.AccountRequest;
import com.banklite.bankliteapi.dto.account.AccountResponse;
import com.banklite.bankliteapi.dto.account.UpdateAccountRequest;
import com.banklite.bankliteapi.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")

public class AccountController {

    AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public AccountResponse createAccount(@RequestBody AccountRequest accountRequest) {
        return accountService.createAccount(accountRequest);
    }

    @GetMapping("/{id}")
    public AccountResponse getAccount(@PathVariable Long id) {
        return accountService.findAccountById(id);
    }

    @PatchMapping("/{id}")
    public AccountResponse updateAccount(@PathVariable Long id, @RequestBody UpdateAccountRequest accountRequest) {
        return accountService.updateAccount(id, accountRequest);
    }
}
