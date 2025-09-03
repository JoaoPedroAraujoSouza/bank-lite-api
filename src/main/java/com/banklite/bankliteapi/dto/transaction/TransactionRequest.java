package com.banklite.bankliteapi.dto.transaction;

import com.banklite.bankliteapi.model.enums.TransactionType;

import java.math.BigDecimal;

public class TransactionRequest {

    private BigDecimal amount;
    private TransactionType type;
    private Long sourceAccountId;
    private Long destinationAccountId;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Long getSourceAccountId() {
        return sourceAccountId;
    }

    public void setSourceAccountId(Long sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public Long getDestinationAccountId() {
        return destinationAccountId;
    }

    public void setDestinationAccountId(Long destinationAccountId) {
        this.destinationAccountId = destinationAccountId;
    }
}
