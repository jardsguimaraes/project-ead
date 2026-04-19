package com.ead.course.controlles;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ead.course.dots.CourseRecordDto;
import com.ead.course.especifications.SpecificationTemplate;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/courses")
public class CourseController {

    final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseRecordDto courseRecordDto) {
        if (courseService.existsByName(courseRecordDto.name())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Course Name is Already Taken!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseRecordDto));
    }

    @SuppressWarnings("null")
    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec spec, Pageable pageable) {
        var coursePageModel = courseService.findAll(spec, pageable);

        if (!coursePageModel.isEmpty()) {
            for (CourseModel course : coursePageModel) {
                course.add(linkTo(methodOn(CourseController.class).getOneCourse(course.getCourseId())).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(coursePageModel);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getOneCourse(@PathVariable(value = "courseId") UUID courseId) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.findById(courseId));
    }

    @Transactional
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "courseId") UUID courseId) {
        courseService.delete(courseService.findById(courseId));
        return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully.");
    }

    @Transactional
    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId") UUID courseId,
            @RequestBody @Valid CourseRecordDto courseRecordDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(courseService.update(courseRecordDto, courseService.findById(courseId)));
    }
}
