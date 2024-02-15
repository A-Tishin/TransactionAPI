package com.mintos.TransactionAPI.service;

import com.mintos.TransactionAPI.persistence.entity.UserEntity;
import com.mintos.TransactionAPI.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity create() {
        return userRepository.save(new UserEntity());
    }

    public Optional<UserEntity> findUser(Long id) {
        return userRepository.findById(id);
    }
}
