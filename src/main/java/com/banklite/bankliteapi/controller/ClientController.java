package com.banklite.bankliteapi.controller;


import com.banklite.bankliteapi.dto.client.ClientRequest;
import com.banklite.bankliteapi.dto.client.ClientResponse;
import com.banklite.bankliteapi.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")

public class ClientController {

    ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ClientResponse createClient(@RequestBody ClientRequest clientRequest) {
        return clientService.createClient(clientRequest);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ClientResponse getClient(@PathVariable Long id) {
        return clientService.findClientById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ClientResponse updateClient(@PathVariable Long id, @RequestBody ClientRequest clientRequest) {
        return clientService.updateClient(id, clientRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
    }
}
