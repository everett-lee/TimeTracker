package com.timetracker.timetracker.service;

import com.timetracker.timetracker.model.Client;
import com.timetracker.timetracker.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {
    private ClientRepository clientRepo;

    public ClientService(ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    @Transactional
    public Client createClient(String clientName, String businessType, String location) {
        Client client = new Client();
        client.setClientName(clientName);
        client.setBusinessType(businessType);
        client.setLocation(location);

        clientRepo.save(client);

        return client;
    }
}
