package com.example.goldengymback.service;

import com.example.goldengymback.model.User;
import java.util.List;

public interface InterfaceClient {
    User saveUser(User user); // uniquement si tu veux ajouter
    List<User> getAllUsers();
    User getUserById(Long id);
    void deleteUser(Long id);
    User loginUser(String nom, String password); // IMPORTANT
}
