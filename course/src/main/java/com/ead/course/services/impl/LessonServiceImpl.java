package com.ead.course.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ead.course.dtos.LessonRecordDto;
import com.ead.course.exceptions.ExternalNotFoundException;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.services.LessonService;

@Service
public class LessonServiceImpl implements LessonService {

    final LessonRepository lessonRepository;

    public LessonServiceImpl(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Override
    public LessonModel save(LessonRecordDto lessonRecordDto, ModuleModel moduleModel) {
        Objects.requireNonNull(lessonRecordDto, "lessonRecordDto cannot be null");
        Objects.requireNonNull(moduleModel, "moduleModel cannot be null");
        var lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonRecordDto, lessonModel);

        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        lessonModel.setModule(moduleModel);

        return lessonRepository.save(lessonModel);
    }

    @Override
    public LessonModel findLessonIntoModule(UUID moduleId, UUID lessonId) {
        return lessonRepository.findLessonIntoModule(moduleId, lessonId)
                .orElseThrow(() -> new ExternalNotFoundException("Error: Lesson Not found!"));
    }

    @Override
    public void delete(LessonModel lessonIntoCourse) {
        Objects.requireNonNull(lessonIntoCourse, "lessonIntoCourse cannot be null");
        lessonRepository.delete(lessonIntoCourse);
    }

    @Override
    public LessonModel update(LessonRecordDto lessonRecordDto, LessonModel lessonModel) {
        Objects.requireNonNull(lessonRecordDto, "lessonRecordDto cannot be null");
        Objects.requireNonNull(lessonModel, "lessonModel cannot be null");
        BeanUtils.copyProperties(lessonRecordDto, lessonModel);
        return lessonRepository.save(lessonModel);
    }

    @Override
    public Page<LessonModel> findAllLessonsIntoModule(Specification<LessonModel> spec, Pageable pageable) {
        Objects.requireNonNull(pageable, "Error: pageable Not found!");
        return lessonRepository.findAll(spec, pageable);
    }
}
