package com.ead.authuser.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ead.authuser.dtos.AdminRecordDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/admins")
public class AdminController {

    private UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/subscription")
    public ResponseEntity<UserModel> registerUserAdmin(@RequestBody @Valid AdminRecordDto adminRecordDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.registerUserAdmin(userService.findById(adminRecordDto.userId())));
    }

}
