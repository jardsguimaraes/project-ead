package com.ead.authuser.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.dtos.CourseRecordDto;
import com.ead.authuser.dtos.UserCourseRecordDto;
import com.ead.authuser.services.UserCourseService;
import com.ead.authuser.services.UserService;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/users")
public class UserCourseController {

    final CourseClient courseClient;
    final UserService userService;
    final UserCourseService userCourseService;

    public UserCourseController(CourseClient courseClient, UserService userService,
            UserCourseService userCourseService) {
        this.courseClient = courseClient;
        this.userService = userService;
        this.userCourseService = userCourseService;
    }

    @GetMapping("/{userId}/courses")
    public ResponseEntity<Page<CourseRecordDto>> getAllCoursesByUser(@PathVariable(value = "userId") UUID userId,
            @PageableDefault(sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable) {
        userService.findById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllCourseByUser(userId, pageable));
    }

    @Transactional
    @PostMapping("/{userId}/courses/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable(value = "userId") UUID userId,
            @RequestBody @Valid UserCourseRecordDto userCourseRecordDto) {
        var courseId = userCourseRecordDto.courseId();
        var userModel = userService.findById(userId);

        if (userCourseService.existsByUserAndCourse(userModel, courseId)) {
            log.error("Error: Subscription already exists! {}", courseId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Subscription already exists!");
        }

        var userCourseModel = userCourseService.save(userModel.convertToUserCourseModel(courseId));

        log.debug("User successfully replicated in authuser: userId={}, courseId={} ",
                userCourseModel.getUser().getUserId(), userCourseModel.getCourseId());
        return ResponseEntity.status(HttpStatus.CREATED).body(userCourseModel);
    }

    @Transactional
    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<Object> deleteUserCourseByCourse(@PathVariable(value = "courseId") UUID courseId) {
        if (!userCourseService.existsByCourseId(courseId)) {
            log.error("Error: Course not found! {}", courseId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Course not found!");
        }

        userCourseService.deleteAllByCourseId(courseId);

        return ResponseEntity.status(HttpStatus.OK).body("UserCourse deleted successfully.");
    }
}