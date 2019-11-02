package com.timetracker.timetracker.service;

import com.timetracker.timetracker.model.Client;
import com.timetracker.timetracker.repository.ClientRepository;
import com.timetracker.timetracker.service.exceptions.ClientNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService {
    private ClientRepository clientRepo;

    public ClientService(ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    @Transactional
    @PreAuthorize("#ownerId == principal.id")
    public Client createClient(Long ownerId, String clientName, String businessType, String location) {
        Client client = new Client();
        client.setOwnerId(ownerId);
        client.setClientName(clientName);
        client.setBusinessType(businessType);
        client.setLocation(location);

        clientRepo.save(client);

        return client;
    }

    @Transactional
    @PreAuthorize("#ownerId == principal.id")
    public boolean deleteClient(Long ownerId, Long clientId) throws ClientNotFoundException {
        Client client = clientRepo.findById(clientId)
                .orElseThrow( () -> new ClientNotFoundException(String
                .format("Client with id: %s not found", clientId)));

        if (client.getOwnerId() != ownerId) {
            throw new AccessDeniedException("User does not have ownership of this Client");
        }

        clientRepo.deleteById(clientId);
        return true;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("#ownerId == principal.id")
    public List<Client> getAllClientsByOwnerId(Long ownerId) {
        return clientRepo.findAllByOwnerId(ownerId);
    }
}
