package com.example.goldengymback.serviceImpl;

import com.example.goldengymback.model.Client;
import com.example.goldengymback.repository.ClientRepository;
import com.example.goldengymback.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service  // Cette annotation permet à Spring de créer un bean de cette classe
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client ajouterClient(Client client) {
        return clientRepository.save(client);
    }
}
