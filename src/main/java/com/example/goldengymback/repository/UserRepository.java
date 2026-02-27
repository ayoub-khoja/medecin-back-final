package com.example.goldengymback.repository;

import com.example.goldengymback.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByNomAndPassword(String nom, String password);
}
