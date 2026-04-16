package com.ead.course.services;

import java.util.List;
import java.util.UUID;

import com.ead.course.dots.LessonRecordDto;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;

public interface LessonService {

    LessonModel save(LessonRecordDto lessonRecordDto, ModuleModel moduleModel);

    List<LessonModel> findAllLessonsIntoModule(UUID moduleId);

    LessonModel findLessonIntoModule(UUID moduleId, UUID lessonId);

    void delete(LessonModel lessonIntoCourse);

    LessonModel update(LessonRecordDto lessonRecordDto, LessonModel lessonModel);

}
