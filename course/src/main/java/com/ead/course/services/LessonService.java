package com.ead.course.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.ead.course.dots.LessonRecordDto;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;

public interface LessonService {

    LessonModel save(LessonRecordDto lessonRecordDto, ModuleModel moduleModel);

    LessonModel findLessonIntoModule(UUID moduleId, UUID lessonId);

    void delete(LessonModel lessonIntoCourse);

    LessonModel update(LessonRecordDto lessonRecordDto, LessonModel lessonModel);

    Page<LessonModel> findAllLessonsIntoModule(Specification<LessonModel> spec, Pageable pageable);
}
