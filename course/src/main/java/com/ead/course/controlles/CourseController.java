package com.ead.course.controlles;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ead.course.dtos.CourseRecordDto;
import com.ead.course.especifications.SpecificationTemplate;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.validations.CourseValidation;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final CourseValidation courseValidation;

    public CourseController(CourseService courseService, CourseValidation courseValidation) {
        this.courseService = courseService;
        this.courseValidation = courseValidation;
    }

    @Transactional
    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody CourseRecordDto courseRecordDto, Errors errors) {
        Objects.requireNonNull(courseRecordDto, "courseRecordDto cannot be null");
        Objects.requireNonNull(errors, "errors cannot be null");
        log.debug("POST saveCourse courseRecordDto received {} ", courseRecordDto);

        courseValidation.validate(courseRecordDto, errors);

        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
        }

        log.debug("Course saved successfully {} ", courseRecordDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseRecordDto));
    }

    @SuppressWarnings("null")
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec spec, Pageable pageable,
            @RequestParam(required = false) UUID userId) {
        var coursePageModel = userId != null
                ? courseService.findAll(SpecificationTemplate.courseUserId(userId).and(spec), pageable)
                : courseService.findAll(spec, pageable);

        if (!coursePageModel.isEmpty()) {
            for (CourseModel course : coursePageModel) {
                course.add(linkTo(methodOn(CourseController.class).getOneCourse(course.getCourseId())).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(coursePageModel);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getOneCourse(@PathVariable(value = "courseId") UUID courseId) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.findById(courseId));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "courseId") UUID courseId) {
        log.debug("DELETE deleteCourse courseId received {} ", courseId);
        courseService.delete(courseService.findById(courseId));

        return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully.");
    }

    @Transactional
    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId") UUID courseId,
            @RequestBody @Valid CourseRecordDto courseRecordDto) {
        log.debug("PUT updateCourse courseRecordDto received {}", courseRecordDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(courseService.update(courseRecordDto, courseService.findById(courseId)));
    }
}
