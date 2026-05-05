package com.ead.course.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ead.course.dots.CourseRecordDto;
import com.ead.course.exceptions.ExternalNotFoundException;
import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.models.UserModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;

    public CourseServiceImpl(CourseRepository courseRepository, LessonRepository lessonRepository,
            ModuleRepository moduleRepository) {
        this.courseRepository = courseRepository;
        this.moduleRepository = moduleRepository;
        this.lessonRepository = lessonRepository;
    }

    @Transactional
    @Override
    public void delete(CourseModel courseModel) {
        List<ModuleModel> moduleModelList = moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());

        if (!moduleModelList.isEmpty()) {
            for (ModuleModel module : moduleModelList) {
                List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModule(module.getModuleId());
                if (!lessonModelList.isEmpty()) {
                    lessonRepository.deleteAll(lessonModelList);
                }
            }
            moduleRepository.deleteAll(moduleModelList);
        }

        courseRepository.delete(courseModel);
    }

    @Override
    public CourseModel save(CourseRecordDto courseRecordDto) {
        Objects.requireNonNull(courseRecordDto, "courseRecordDto cannot be null");
        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseRecordDto, courseModel);

        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return courseRepository.save(courseModel);
    }

    @Override
    public boolean existsByName(String name) {
        return courseRepository.existsByName(name);
    }

    @Override
    public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
        Objects.requireNonNull(pageable, "pageable cannot be null");
        return courseRepository.findAll(spec, pageable);
    }

    @Override
    public CourseModel findById(UUID courseId) {
        Objects.requireNonNull(courseId, "courseId cannot be null");
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ExternalNotFoundException("Error: Course Not found!"));
    }

    @Override
    public CourseModel update(CourseRecordDto courseRecordDto, CourseModel courseModel) {
        Objects.requireNonNull(courseRecordDto, "courseRecordDto cannot be null");
        Objects.requireNonNull(courseModel, "courseModel cannot be null");
        BeanUtils.copyProperties(courseRecordDto, courseModel);
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return courseRepository.save(courseModel);
    }

    @Override
    public boolean existsByCourseAndUser(UUID courseId, UUID userId) {
        return courseRepository.existsByCourseAndUser(courseId, userId);
    }

    @Override
    public void saveSubscriptionUserInCourse(CourseModel courseModel, UserModel userModel) {
        courseRepository.saveCourseUser(courseModel.getCourseId(), userModel.getUserId());
    }
}
