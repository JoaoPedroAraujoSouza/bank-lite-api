package com.banklite.bankliteapi.service;

import com.banklite.bankliteapi.dto.account.AccountRequest;
import com.banklite.bankliteapi.dto.account.AccountResponse;
import com.banklite.bankliteapi.dto.account.UpdateAccountRequest;
import com.banklite.bankliteapi.dto.client.ClientResponse;
import com.banklite.bankliteapi.model.Account;
import com.banklite.bankliteapi.model.Client;
import com.banklite.bankliteapi.repository.AccountRepository;
import com.banklite.bankliteapi.repository.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    public AccountService(AccountRepository accountRepository, ClientRepository clientRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
    }

    public AccountResponse createAccount(AccountRequest accountRequest) {

        Client client = clientRepository.findById(accountRequest.getClientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found with id:" + accountRequest.getClientId()));

        Account newAccount = new Account();

        newAccount.setClient(client);
        newAccount.setBalance(new BigDecimal(0.00));
        newAccount.setIsBlocked(false);

        Account savedAccount = accountRepository.save(newAccount);

        return mapToAccountResponse(savedAccount);
    }

    public AccountResponse findAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found with id: " + id));

        return mapToAccountResponse(account);
    }

    public AccountResponse updateAccount(long id, UpdateAccountRequest request) {
        Account accountToUpdate = accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found with id: " + id));

        accountToUpdate.setIsBlocked(request.getIsBlocked());

        Account updatedAccount = accountRepository.save(accountToUpdate);

        return mapToAccountResponse(updatedAccount);
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
