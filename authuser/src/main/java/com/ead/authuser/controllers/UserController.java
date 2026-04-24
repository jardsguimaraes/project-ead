package com.ead.authuser.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ead.authuser.dtos.UserRecordDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserServices;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.extern.log4j.Log4j2;
import specifications.SpecificationTemplate;

@Log4j2
@RestController
@RequestMapping("/users")
public class UserController {

    final UserServices userService;

    public UserController(UserServices userService) {
        this.userService = userService;
    }

    @SuppressWarnings("null")
    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(SpecificationTemplate.UserSpec spec, Pageable pageable,
            @RequestParam(required = false) UUID courseId) {
        var userPageModel = courseId != null
                ? userService.findAll(SpecificationTemplate.userCourseId(courseId).and(spec), pageable)
                : userService.findAll(spec, pageable);

        if (!userPageModel.isEmpty()) {
            for (UserModel user : userPageModel) {
                user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(userPageModel);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "userId") UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(userId).get());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId) {
        log.debug("DELETE deleteUser userId received {}", userId);
        userService.delete(userService.findById(userId).get());

        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @Validated(UserRecordDto.UserView.UserPut.class) @JsonView(UserRecordDto.UserView.UserPut.class) UserRecordDto userRecordDto) {
        log.debug("PUT updateUser userRecordDto received {}", userRecordDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateUser(userRecordDto, userService.findById(userId).get()));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @Validated(UserRecordDto.UserView.PasswordPut.class) @JsonView(UserRecordDto.UserView.PasswordPut.class) UserRecordDto userRecordDto) {
        log.debug("PUT updatePassword userRecordDto received {}", userRecordDto);
        Optional<UserModel> userModelOptional = userService.findById(userId);

        if (!userModelOptional.get().getPassword().equals(userRecordDto.oldPassword())) {
            log.warn("Mismatched old password! userId {} ", userId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismarched old password!");
        }

        userService.updatePassword(userRecordDto, userModelOptional.get());
        log.debug("updatePassword registered successfully for userId: {}", userId);

        return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully.");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @Validated(UserRecordDto.UserView.ImagePut.class) @JsonView(UserRecordDto.UserView.ImagePut.class) UserRecordDto userRecordDto) {
        log.debug("PUT updateImage userRecordDto received {}", userRecordDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateImage(userRecordDto, userService.findById(userId).get()));
    }
}
