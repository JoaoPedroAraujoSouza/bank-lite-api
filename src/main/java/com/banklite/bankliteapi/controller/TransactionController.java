package com.banklite.bankliteapi.controller;

import com.banklite.bankliteapi.dto.transaction.TransactionRequest;
import com.banklite.bankliteapi.dto.transaction.TransactionResponse;
import com.banklite.bankliteapi.service.TransactionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")

public class TransactionController {

    TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public TransactionResponse deposit(@RequestBody TransactionRequest transactionRequest) {
        return transactionService.performDeposit(transactionRequest);
    }

    @PostMapping("/withdrawal")
    public TransactionResponse withdrawal(@RequestBody TransactionRequest transactionRequest) {
        return transactionService.performWithdrawal(transactionRequest);
    }

    @PostMapping("/transfer")
    public TransactionResponse transfer(@RequestBody TransactionRequest transactionRequest) {
        return transactionService.performTransfer(transactionRequest);
    }
}
