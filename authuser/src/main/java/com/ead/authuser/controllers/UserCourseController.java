package com.ead.authuser.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.dtos.CourseRecordDto;
import com.ead.authuser.services.UserService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/users")
public class UserCourseController {

    final CourseClient courseClient;
    final UserService userService;

    public UserCourseController(CourseClient courseClient, UserService userService) {
        this.courseClient = courseClient;
        this.userService = userService;
    }

    @PreAuthorize("@userAuthorization.isCurrentUserOrAdmin(#userId)")
    @GetMapping("/{userId}/courses")
    public ResponseEntity<Page<CourseRecordDto>> getAllCoursesByUser(@PathVariable(value = "userId") UUID userId,
            @PageableDefault(sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestHeader("Authorization") String token) {
        userService.findById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllCourseByUser(userId, pageable, token));
    }
}