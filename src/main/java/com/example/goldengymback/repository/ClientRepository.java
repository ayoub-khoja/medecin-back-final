package com.example.goldengymback.repository;

import com.example.goldengymback.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    // Nous pouvons ajouter des méthodes personnalisées si nécessaire
}
