package com.ead.course.controlles;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.dots.SubscriptionRecordDto;
import com.ead.course.dots.UserRecordDto;
import com.ead.course.enums.UserStatus;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/courses")
public class CourseUserController {

    final AuthUserClient authUserClient;
    final CourseService courseService;
    final CourseUserService courseUserService;

    public CourseUserController(AuthUserClient authUserClient, CourseService courseService,
            CourseUserService courseUserService) {
        this.authUserClient = authUserClient;
        this.courseService = courseService;
        this.courseUserService = courseUserService;
    }

    @GetMapping("/{courseId}/users")
    public ResponseEntity<Page<UserRecordDto>> getAllUsersByCourse(
            @PageableDefault(sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable(name = "courseId") UUID courseId) {
        courseService.findById(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(authUserClient.getAllUsersByCourse(courseId, pageable));
    }

    @Transactional
    @PostMapping("/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable(value = "courseId") UUID courseId,
            @RequestBody @Valid SubscriptionRecordDto subscriptionRecordDto) {
        var userId = subscriptionRecordDto.userId();
        var courseModel = courseService.findById(courseId);

        if (courseUserService.existsByCourseAndUserId(courseModel, userId)) {
            log.error("Error: Subscription already exists {}", courseId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Subscription already exists");
        }

        var responseUser = authUserClient.getOneUserById(userId);
        if (responseUser.userStatus().equals(UserStatus.BLOCKED)) {
            log.error("Error: User is blocked");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: User is blocked");
        }

        var courseUserModel = courseUserService.saveAndSendSubscriptionUserInCourse(
                courseModel.convertToCourseUserModel(userId));

        log.debug("User successfully enrolled: userId={}, courseId={} ", courseUserModel.getUserId(),
                courseUserModel.getCourse().getCourseId());
        return ResponseEntity.status(HttpStatus.CREATED).body(courseUserModel);
    }
}
