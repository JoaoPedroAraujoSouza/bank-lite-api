package com.banklite.bankliteapi.dto.transaction;

import com.banklite.bankliteapi.dto.account.AccountResponse;
import com.banklite.bankliteapi.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public class TransactionResponse {

    private Long id;
    private BigDecimal amount;
    private TransactionType transactionType;
    private AccountResponse sourceAccount;
    private AccountResponse destinationAccount;
    private Instant createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public AccountResponse getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(AccountResponse sourceAccountId) {
        this.sourceAccount = sourceAccountId;
    }

    public AccountResponse getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(AccountResponse destinationAccountId) {
        this.destinationAccount = destinationAccountId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
