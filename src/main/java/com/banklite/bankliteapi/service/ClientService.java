package com.banklite.bankliteapi.service;

import com.banklite.bankliteapi.dto.client.ClientRequest;
import com.banklite.bankliteapi.dto.client.ClientResponse;
import com.banklite.bankliteapi.model.Client;
import com.banklite.bankliteapi.repository.ClientRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientResponse createClient(ClientRequest clientRequest) {

        Client newClient = new Client();

        newClient.setName(clientRequest.getName());
        newClient.setEmail(clientRequest.getEmail());
        newClient.setCpf(clientRequest.getCpf());

        newClient.setCreatedAt(Instant.now());

        Client savedClient = clientRepository.save(newClient);

        return mapToClientResponse(savedClient);
    }

    public ClientResponse findClientById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found with id: " + id));

        return mapToClientResponse(client);
    }

    public ClientResponse updateClient(long id, ClientRequest clientRequest) {
        Client clientToUpdate = clientRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found with id: " + id));

        clientToUpdate.setName(clientRequest.getName());
        clientToUpdate.setEmail(clientRequest.getEmail());
        clientToUpdate.setCpf(clientRequest.getCpf());

        Client updatedClient = clientRepository.save(clientToUpdate);

        return mapToClientResponse(updatedClient);
    }

    public void deleteClient(long id) {
        Client clientToDelete = clientRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found with id: " + id));
        try {
            clientRepository.delete(clientToDelete);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete client with id: " + id + " because it has associated accounts.");
        }
    }

    public ClientResponse mapToClientResponse(Client client) {

        ClientResponse clientResponse = new ClientResponse();

        clientResponse.setId(client.getId());
        clientResponse.setName(client.getName());
        clientResponse.setEmail(client.getEmail());
        clientResponse.setCpf(client.getCpf());
        clientResponse.setCreatedAt(client.getCreatedAt());

        return clientResponse;
    }
}
