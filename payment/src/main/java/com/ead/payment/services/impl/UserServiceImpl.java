package com.ead.payment.services.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ead.payment.models.UserModel;
import com.ead.payment.repositories.UserRepository;
import com.ead.payment.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserModel save(UserModel userModel) {
        return userRepository.save(userModel);
    }

    @Override
    public void delete(UUID userId) {
        userRepository.deleteById(userId);
    }
    
}
