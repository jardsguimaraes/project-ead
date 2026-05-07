package com.ead.course.services.impl;

import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ead.course.exceptions.ExternalNotFoundException;
import com.ead.course.models.UserModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.UserRepository;
import com.ead.course.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public UserServiceImpl(UserRepository userRepository, CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable) {
        Objects.requireNonNull(pageable, "pageable cannot be null");
        return userRepository.findAll(spec, pageable);
    }

    @Transactional
    @Override
    public UserModel save(UserModel userModel) {
        Objects.requireNonNull(userModel, "User model cannot be null");
        return userRepository.save(userModel);
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        Objects.requireNonNull(userId, "userId cannot be null");
        courseRepository.deleteCourseUserByUser(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public UserModel findById(UUID userId) {
        Objects.requireNonNull(userId, "userId cannot be null");
        return userRepository.findById(userId)
                .orElseThrow(() -> new ExternalNotFoundException("Error: User not found!"));
    }
}
