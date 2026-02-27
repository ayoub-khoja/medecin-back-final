package com.example.goldengymback.controller;

import com.example.goldengymback.model.Client;
import com.example.goldengymback.service.ClientService; // Vérifiez que c'est bien importé
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/enregistrer")
    public ResponseEntity<Client> ajouterClient(@RequestBody Client client) {
        Client savedClient = clientService.ajouterClient(client);
        return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    }
}
