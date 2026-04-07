package com.ead.authuser.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ead.authuser.exceptions.NotFoundException;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.UserServices;

@Service
public class UserServiceImpl implements UserServices {

    final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserModel> findById(UUID userId) {
        Optional<UserModel> userModeOptional = userRepository.findById(userId);

        if (userModeOptional.isEmpty()) {
            throw new NotFoundException("Error User not found!");
        }

        return userModeOptional;
    }

    @Override
    public void delete(UserModel userModel) {
        userRepository.delete(userModel);
    }

}
