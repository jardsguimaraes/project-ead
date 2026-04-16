package com.ead.course.services;

import java.util.List;
import java.util.UUID;

import com.ead.course.dots.CourseRecordDto;
import com.ead.course.models.CourseModel;

public interface CourseService {

    void delete(CourseModel courseModel);

    CourseModel save(CourseRecordDto courseRecordDto);

    boolean existsByName(String name);

    List<CourseModel> findAll();

    CourseModel findById(UUID courseId);

    CourseModel update(CourseRecordDto courseRecordDto, CourseModel courseModel);
}