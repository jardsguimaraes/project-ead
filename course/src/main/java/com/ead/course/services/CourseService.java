package com.ead.course.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.ead.course.dtos.CourseRecordDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.UserModel;

public interface CourseService {

    void delete(CourseModel courseModel);

    CourseModel save(CourseRecordDto courseRecordDto);

    boolean existsByName(String name);

    Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable);

    CourseModel findById(UUID courseId);

    CourseModel update(CourseRecordDto courseRecordDto, CourseModel courseModel);

    boolean existsByCourseAndUser(UUID courseId, UUID userId);

    void saveSubscriptionUserInCourse(CourseModel courseModel, UserModel userModel);
}