package com.ead.authuser.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ead.authuser.dtos.InstructorrecordDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/instructors")
public class InstructorController {

    final UserService userService;

    public InstructorController(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @PostMapping("/subscription")
    public ResponseEntity<UserModel> putMethodName(@RequestBody @Valid InstructorrecordDto instructorrecordDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.registerInstructor(userService.findById(instructorrecordDto.userId())));
    }
}
