package com.ead.authuser.services.impl;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserCourseRepository;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.UserCourseService;

@Service
public class UserCourseServiceImpl implements UserCourseService {

    final UserRepository userRepository;
    final UserCourseRepository userCourseRepository;

    public UserCourseServiceImpl(UserCourseRepository userCourseRepository, UserRepository userRepository) {
        this.userCourseRepository = userCourseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean existsByUserAndCourse(UserModel userModel, UUID courseId) {
        return userCourseRepository.existsByUserAndCourseId(userModel, courseId);
    }

    @Override
    public UserCourseModel save(UserCourseModel userCourseModel) {
        Objects.requireNonNull(userCourseModel, "userCourseModel cannot be null");
        return userCourseRepository.save(userCourseModel);
    }

    @Override
    public boolean existsByCourseId(UUID courseId) {
        return userCourseRepository.existsByCourseId(courseId);
    }

    @Override
    public void deleteAllByCourseId(UUID courseId) {
        userCourseRepository.deleteAllByCourseId(courseId);
    }
}
