package com.ead.course.services.impl;

import org.springframework.stereotype.Service;

import com.ead.course.repositories.CourseRepository;
import com.ead.course.services.CourseService;

@Service
public class CourseServiceImpl implements CourseService {

    final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    

}
