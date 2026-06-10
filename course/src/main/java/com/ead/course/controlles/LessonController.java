package com.ead.course.controlles;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ead.course.dtos.LessonRecordDto;
import com.ead.course.especifications.SpecificationTemplate;
import com.ead.course.models.LessonModel;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/modules")
public class LessonController {

    final ModuleService moduleService;
    final LessonService lessonService;

    public LessonController(ModuleService moduleService, LessonService lessonService) {
        this.moduleService = moduleService;
        this.lessonService = lessonService;
    }

    @Transactional
    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @PostMapping("/{moduleId}/lessons")
    public ResponseEntity<Object> saveLesson(@PathVariable(value = "moduleId") UUID moduleId,
            @RequestBody @Valid LessonRecordDto lessonRecordDto) {
        log.debug("POST saveCourse courseRecordDto received {} ", lessonRecordDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(lessonService.save(lessonRecordDto, moduleService.findById(moduleId)));
    }

    @SuppressWarnings("null")
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{moduleId}/lessons")
    public ResponseEntity<Page<LessonModel>> getAllLessons(@PathVariable(value = "moduleId") UUID moduleId,
            SpecificationTemplate.LessonSpec spec, Pageable pageable) {
        var lessonPageModel = lessonService
                .findAllLessonsIntoModule(SpecificationTemplate.lessonModuleId(moduleId).and(spec), pageable);

        if (!lessonPageModel.isEmpty()) {
            for (LessonModel lesson : lessonPageModel) {
                lesson.add(linkTo(methodOn(LessonController.class).getOneLessons(moduleId, lesson.getLessonId()))
                        .withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(lessonPageModel);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> getOneLessons(@PathVariable(value = "moduleId") UUID moduleId,
            @PathVariable(value = "lessonId") UUID lessonId) {
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.findLessonIntoModule(moduleId, lessonId));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @DeleteMapping("/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> deleteLesson(@PathVariable(value = "moduleId") UUID moduleId,
            @PathVariable(value = "lessonId") UUID lessonId) {
        log.debug("DELETE deleteCourse courseId received {} ", lessonId);
        lessonService.delete(lessonService.findLessonIntoModule(moduleId, lessonId));
        return ResponseEntity.status(HttpStatus.OK).body("Lesson deleted successfully.");
    }

    @Transactional
    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @PutMapping("/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> updateLesson(@PathVariable(value = "moduleId") UUID moduleId,
            @PathVariable(value = "lessonId") UUID lessonId, @RequestBody @Valid LessonRecordDto lessonRecordDto) {
        log.debug("PUT updateCourse courseRecordDto received {}", lessonRecordDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(lessonService.update(lessonRecordDto, lessonService.findLessonIntoModule(moduleId, lessonId)));
    }
}
