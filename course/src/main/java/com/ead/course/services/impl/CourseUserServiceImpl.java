package com.ead.course.services.impl;

import org.springframework.stereotype.Service;

import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.services.CourseUserService;

@Service
public class CourseUserServiceImpl implements CourseUserService {

    final CourseUserRepository courseUserRepository;

    public CourseUserServiceImpl(CourseUserRepository courseUserRepository) {
        this.courseUserRepository = courseUserRepository;
    }
}
