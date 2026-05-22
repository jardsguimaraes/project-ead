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
import com.ead.course.dots.NotificationRecordCommandDto;
import com.ead.course.exceptions.ExternalNotFoundException;
import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.models.UserModel;
import com.ead.course.publisher.NotificationCommandPublisher;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CourseServiceImpl implements CourseService {

    private final NotificationCommandPublisher notificationCommandPublisher;
    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;

    public CourseServiceImpl(CourseRepository courseRepository, LessonRepository lessonRepository,
            ModuleRepository moduleRepository, NotificationCommandPublisher notificationCommandPublisher) {
        this.courseRepository = courseRepository;
        this.moduleRepository = moduleRepository;
        this.lessonRepository = lessonRepository;
        this.notificationCommandPublisher = notificationCommandPublisher;
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

        courseRepository.deleteCourseUserByCourse(courseModel.getCourseId());
        courseRepository.delete(courseModel);
    }

    @Transactional
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

    @Transactional
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

        try {
            var notificationRecordCommandDto = new NotificationRecordCommandDto(
                    "Bem-vindo(a) ao curso: " + courseModel.getName(),
                    userModel.getFullName() + " a sua inscrição foi realizada com sucesso!",
                    userModel.getUserId());
            notificationCommandPublisher.publishNotificationCommand(notificationRecordCommandDto);
        } catch (Exception e) {
            log.error("Error sending notification!");
        }
    }
}
