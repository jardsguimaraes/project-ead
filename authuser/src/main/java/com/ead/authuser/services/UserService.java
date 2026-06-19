package com.ead.authuser.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.ead.authuser.dtos.UserRecordDto;
import com.ead.authuser.models.UserModel;

public interface UserService {

    List<UserModel> findAll();

    UserModel findById(UUID userId);

    void delete(UserModel userModel);

    UserModel registerUser(UserRecordDto userRecordDto);

    boolean existsByUserName(String username);

    boolean existsByEmail(String email);

    UserModel updateUser(UserRecordDto userRecordDto, UserModel userModel);

    UserModel updatePassword(UserRecordDto userRecordDto, UserModel userModel);

    UserModel updateImage(UserRecordDto userRecordDto, UserModel userModel);

    Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable);

    UserModel registerInstructor(UserModel userModel);

    UserModel registerUserAdmin(UserModel userModel);

}
