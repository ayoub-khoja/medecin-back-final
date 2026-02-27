package com.example.goldengymback.serviceImpl;

import com.example.goldengymback.model.User;
import com.example.goldengymback.repository.UserRepository;
import com.example.goldengymback.service.InterfaceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServImpClient implements InterfaceClient {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User loginUser(String nom, String password) {
        return userRepository.findByNomAndPassword(nom, password);
    }
}
