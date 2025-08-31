package com.banklite.bankliteapi.service;

import com.banklite.bankliteapi.dto.transaction.TransactionRequest;
import com.banklite.bankliteapi.dto.transaction.TransactionResponse;
import com.banklite.bankliteapi.model.Account;
import com.banklite.bankliteapi.model.Client;
import com.banklite.bankliteapi.model.Transaction;
import com.banklite.bankliteapi.model.enums.TransactionType;
import com.banklite.bankliteapi.repository.AccountRepository;
import com.banklite.bankliteapi.repository.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    @DisplayName("Needs to throw exception when trying to withdraw with insufficient balance")
    void performWithdrawal_ShouldThrowException_WhenBalanceIsInsufficient() {
        TransactionRequest request = new TransactionRequest();
        request.setSourceAccountId(1L);
        request.setAmount(new BigDecimal("200.00"));

        Account sourceAccount = new Account();
        sourceAccount.setId(1L);
        sourceAccount.setBalance(new BigDecimal("100.00"));
        sourceAccount.setIsBlocked(false);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            transactionService.performWithdrawal(request);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Insufficient funds in source account", exception.getReason());
    }

    @Test
    @DisplayName("Needs to successfully withdraw when there is sufficient balance")
    void performWithdrawal_ShouldSucceed_WhenBalanceIsSufficient() {
        TransactionRequest request = new TransactionRequest();
        request.setSourceAccountId(1L);
        request.setAmount(new BigDecimal("150.00"));

        Client client = new Client();
        client.setId(1L);
        client.setName("Test Client");

        Account sourceAccount = new Account();
        sourceAccount.setId(1L);
        sourceAccount.setBalance(new BigDecimal("500.00"));
        sourceAccount.setIsBlocked(false);
        sourceAccount.setClient(client);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TransactionResponse response = transactionService.performWithdrawal(request);

        assertNotNull(response);
        assertEquals(TransactionType.WITHDRAWAL, response.getTransactionType());
        assertEquals(0, new BigDecimal("150.00").compareTo(response.getAmount()));
        assertEquals(0, new BigDecimal("350.00").compareTo(response.getSourceAccount().getBalance()));

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository, times(1)).save(accountCaptor.capture());
        assertEquals(0, new BigDecimal("350.00").compareTo(accountCaptor.getValue().getBalance()));
    }

    @Test
    @DisplayName("Needs to throw exception when trying to transfer with insufficient balance")
    void performTransfer_ShouldThrowException_WhenSourceAccountBalanceIsInsufficient() {
        TransactionRequest request = new TransactionRequest();
        request.setSourceAccountId(1L);
        request.setDestinationAccountId(2L);
        request.setAmount(new BigDecimal("1000.00"));

        Client sourceClient = new Client();
        sourceClient.setId(1L);
        Client destClient = new Client();
        destClient.setId(2L);

        Account sourceAccount = new Account();
        sourceAccount.setId(1L);
        sourceAccount.setBalance(new BigDecimal("500.00"));
        sourceAccount.setIsBlocked(false);
        sourceAccount.setClient(sourceClient);

        Account destinationAccount = new Account();
        destinationAccount.setId(2L);
        destinationAccount.setBalance(new BigDecimal("2000.00"));
        destinationAccount.setIsBlocked(false);
        destinationAccount.setClient(destClient);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(destinationAccount));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            transactionService.performTransfer(request);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Insufficient funds for this transfer.", exception.getReason());
    }
}
