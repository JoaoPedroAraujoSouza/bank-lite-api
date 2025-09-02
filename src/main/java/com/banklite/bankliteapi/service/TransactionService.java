package com.banklite.bankliteapi.service;

import com.banklite.bankliteapi.dto.account.AccountResponse;
import com.banklite.bankliteapi.dto.client.ClientResponse;
import com.banklite.bankliteapi.dto.transaction.TransactionRequest;
import com.banklite.bankliteapi.dto.transaction.TransactionResponse;
import com.banklite.bankliteapi.exception.BusinessException;
import com.banklite.bankliteapi.exception.ResourceNotFoundException;
import com.banklite.bankliteapi.model.Account;
import com.banklite.bankliteapi.model.Transaction;
import com.banklite.bankliteapi.model.enums.TransactionType;
import com.banklite.bankliteapi.repository.AccountRepository;
import com.banklite.bankliteapi.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public TransactionResponse performDeposit(TransactionRequest request) {
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Deposit amount must be positive");
        }
        if (request.getDestinationAccountId() == null) {
            throw new BusinessException("Destination account ID is required for a deposit");
        }

        Account destinationAccount = accountRepository.findById(request.getDestinationAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Destination account not found"));

        if (destinationAccount.getIsBlocked()) {
            throw  new BusinessException("Destination account is blocked");
        }

        destinationAccount.setBalance(destinationAccount.getBalance().add(request.getAmount()));
        Account updatedAccount = accountRepository.save(destinationAccount);

        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setDestinationAccount(updatedAccount);
        transaction.setCreatedAt(Instant.now());
        Transaction savedTransaction = transactionRepository.save(transaction);

        return mapToTransactionResponse(savedTransaction);
    }

    @Transactional
    public TransactionResponse performWithdrawal(TransactionRequest request) {

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Withdrawal amount must be positive");
        }

        if (request.getSourceAccountId() == null) {
            throw new BusinessException("Source account ID is required for a withdrawal");
        }

        Account sourceAccount = accountRepository.findById(request.getSourceAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Source account not found"));

        if (sourceAccount.getIsBlocked()) {
            throw  new BusinessException("Source account is blocked");
        }

        if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new BusinessException("Insufficient funds for this withdrawal.");
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
        Account updatedAccount = accountRepository.save(sourceAccount);

        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setSourceAccount(updatedAccount);
        transaction.setCreatedAt(Instant.now());

        Transaction savedTransaction = transactionRepository.save(transaction);

        return mapToTransactionResponse(savedTransaction);
    }

    @Transactional
    public TransactionResponse performTransfer(TransactionRequest request) {

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Transfer amount must be positive");
        }

        if (request.getSourceAccountId() == null || request.getDestinationAccountId() == null) {
            throw new BusinessException("Both source and destination account IDs are required for a transfer");
        }

        if (request.getSourceAccountId().equals(request.getDestinationAccountId())) {
            throw new BusinessException("Source and destination accounts must be different for a transfer");
        }

        Account sourceAccount = accountRepository.findById(request.getSourceAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Source account not found"));

        Account destinationAccount = accountRepository.findById(request.getDestinationAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Destination account not found"));

        if (sourceAccount.getIsBlocked() || destinationAccount.getIsBlocked()) {
            throw new BusinessException("One or both accounts are blocked");
        }

        if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new BusinessException("Insufficient funds for this transfer.");
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
        destinationAccount.setBalance(destinationAccount.getBalance().add(request.getAmount()));

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        Transaction transaction = new Transaction();

        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.TRANSFER);
        transaction.setSourceAccount(sourceAccount);
        transaction.setDestinationAccount(destinationAccount);
        transaction.setCreatedAt(Instant.now());

        Transaction savedTransaction = transactionRepository.save(transaction);

        return mapToTransactionResponse(savedTransaction);
    }

    public List<TransactionResponse> getStatement(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new ResourceNotFoundException("Account not found");
        }

        List<Transaction> transactions = transactionRepository.findBySourceAccountIdOrDestinationAccountId(accountId, accountId);

        return transactions.stream()
                .map(this::mapToTransactionResponse)
                .toList();
    }

    private TransactionResponse mapToTransactionResponse(Transaction transaction) {

        TransactionResponse response = new TransactionResponse();

        response.setId(transaction.getId());
        response.setAmount(transaction.getAmount());
        response.setTransactionType(transaction.getType());
        response.setCreatedAt(transaction.getCreatedAt());

        if (transaction.getSourceAccount() != null) {
            response.setSourceAccount(mapToAccountResponse(transaction.getSourceAccount()));
        }
        if (transaction.getDestinationAccount() != null) {
            response.setDestinationAccount(mapToAccountResponse(transaction.getDestinationAccount()));
        }

        return response;
    }

    private AccountResponse mapToAccountResponse(Account account) {

        AccountResponse response = new AccountResponse();

        response.setId(account.getId());
        response.setBalance(account.getBalance());
        response.setBlocked(account.getIsBlocked());

        ClientResponse clientResponse = new ClientResponse();

        clientResponse.setId(account.getClient().getId());
        clientResponse.setName(account.getClient().getName());
        clientResponse.setEmail(account.getClient().getEmail());
        clientResponse.setCpf(account.getClient().getCpf());
        clientResponse.setCreatedAt(account.getClient().getCreatedAt());

        response.setClient(clientResponse);

        return response;
    }
}
