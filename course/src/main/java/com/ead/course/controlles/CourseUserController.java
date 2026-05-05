package com.ead.course.controlles;

import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ead.course.dots.SubscriptionRecordDto;
import com.ead.course.dots.SubscriptionValidationRecordDto;
import com.ead.course.especifications.SpecificationTemplate;
import com.ead.course.services.CourseService;
import com.ead.course.services.UserService;
import com.ead.course.validations.SubscriptionUserInCourseValidation;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/courses")
public class CourseUserController {

    private final CourseService courseService;
    private final UserService userService;
    private final SubscriptionUserInCourseValidation subscriptionUserInCourseValidation;

    public CourseUserController(CourseService courseService, UserService userService,
            SubscriptionUserInCourseValidation subscriptionUserInCourseValidation) {
        this.courseService = courseService;
        this.userService = userService;
        this.subscriptionUserInCourseValidation = subscriptionUserInCourseValidation;
    }

    @GetMapping("/{courseId}/users")
    public ResponseEntity<Object> getAllUsersByCourse(
            SpecificationTemplate.UserSpec spec,
            @PageableDefault(sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable(name = "courseId") UUID courseId) {
        courseService.findById(courseId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.findAll(SpecificationTemplate.userCourseId(courseId).and(spec), pageable));
    }

    @Transactional
    @PostMapping("/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable(value = "courseId") UUID courseId,
            @RequestBody @Valid SubscriptionRecordDto subscriptionRecordDto,
            Errors errors) { // refatorar com custom validation
        Objects.requireNonNull(errors, "errors cannot be null");
        var userId = subscriptionRecordDto.userId();
        var courseModel = courseService.findById(courseId);
        var userModel = userService.findById(userId);

        subscriptionUserInCourseValidation.validate(new SubscriptionValidationRecordDto(courseId, userId, userModel.getUserStatus()), errors);
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
        }

        courseService.saveSubscriptionUserInCourse(courseModel, userModel);
        log.debug("Subscription created successfully!");
        return ResponseEntity.status(HttpStatus.CREATED).body("Subscription created successfully!");
    }
}
