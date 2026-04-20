package com.ead.authuser.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ead.authuser.dtos.UserRecordDto;
import com.ead.authuser.services.UserServices;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    // Logger logger = LogManager.getLogger(AuthenticationController.class);
    // --Caso não utilize o Lombok(@Log4j2) para criar o logger, é necessário criar
    // manualmente.

    final UserServices userService;

    public AuthenticationController(UserServices userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(
            @RequestBody @Validated(UserRecordDto.UserView.RegistrationPost.class) @JsonView(UserRecordDto.UserView.RegistrationPost.class) UserRecordDto userRecordDto) {
        log.debug("POST registerUser userRecordDto {}", userRecordDto);

        if (userService.existsByUserName(userRecordDto.username())) {
            log.warn("Username {} is Already taken ", userRecordDto.username());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is already taken!");
        }

        if (userService.existsByEmail(userRecordDto.email())) {
            log.warn("Email {} is Already taken ", userRecordDto.email());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email is already taken!");
        }

        log.debug("User registered successfully: {}", userRecordDto.username());
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userRecordDto));
    }

    // Exemplos de logger utilizando o Lombok(@Log4j2):
    @GetMapping("logs")
    public String index() {
        log.trace("This is a trace message");
        log.debug("This is a debug message");
        log.info("This is an info message");
        log.warn("This is a warning message");
        log.error("This is an error message");
        return "Hello, World!";
    }
}
