package com.ead.course.services.impl;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.services.CourseUserService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CourseUserServiceImpl implements CourseUserService {

    final CourseUserRepository courseUserRepository;
    final AuthUserClient authUserClient;

    public CourseUserServiceImpl(CourseUserRepository courseUserRepository, AuthUserClient authUserClient) {
        this.courseUserRepository = courseUserRepository;
        this.authUserClient = authUserClient;
    }

    @Override
    public boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId) {
        return courseUserRepository.existsByCourseAndUserId(courseModel, userId);
    }

    @Override
    public CourseUserModel saveAndSendSubscriptionUserInCourse(CourseUserModel courseUserModel) {
        Objects.requireNonNull(courseUserModel, "courseUserModel cannot be null");
        courseUserModel = courseUserRepository.save(courseUserModel);
        authUserClient.postSubscriptionUserInCourse(courseUserModel.getCourse().getCourseId(),
                courseUserModel.getUserId());

        return courseUserModel;
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return courseUserRepository.existsByUserId(userId);
    }

    @Override
    public void deleteAllByUserId(UUID userId) {
        courseUserRepository.deleteAllByUserId(userId);
    }
}
