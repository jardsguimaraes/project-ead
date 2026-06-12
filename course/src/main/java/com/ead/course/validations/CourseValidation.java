package com.ead.course.validations;

import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ead.course.configs.security.AuthenticationCurrentUserService;
import com.ead.course.dtos.CourseRecordDto;
import com.ead.course.enums.UserType;
import com.ead.course.services.CourseService;
import com.ead.course.services.UserService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CourseValidation implements Validator {

    private final Validator validator;
    private final CourseService courseService;
    private final UserService userService;
    private final AuthenticationCurrentUserService authenticationCurrentUserService;

    public CourseValidation(Validator validator, CourseService courseService, UserService userService,
            AuthenticationCurrentUserService authenticationCurrentUserService) {
        this.validator = validator;
        this.courseService = courseService;
        this.userService = userService;
        this.authenticationCurrentUserService = authenticationCurrentUserService;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(@NonNull Object object, @NonNull Errors errors) {
        var courseRecordDto = (CourseRecordDto) object;

        validator.validate(courseRecordDto, errors);

        if (!errors.hasErrors()) {
            validateCourseName(courseRecordDto, errors);
            validateUserInstructor(courseRecordDto.userInstructor(), errors);
        }

    }

    private void validateCourseName(CourseRecordDto courseRecordDto, Errors errors) {
        if (courseService.existsByName(courseRecordDto.name())) {
            errors.rejectValue("name", "courseNameConflit", "Course Name is already taken.");
            log.error("Error validation Coursename {}", courseRecordDto.name());
        }
    }

    private void validateUserInstructor(UUID userInstructor, Errors errors) {
        var userDetails = authenticationCurrentUserService.getCurrentUser();

        if (userDetails.getUserId().equals(userInstructor)
                || userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            var userModel = userService.findById(userInstructor);

            if (userModel.getUserType().equals(UserType.STRUDENT.toString())
                    || userModel.getUserType().equals(UserType.USER.toString())) {
                errors.rejectValue("userInstructor", "userInstructorError", "User must be INSTRUCTOR or ADMIN");
                log.error("Error validation userINstructor {}", userModel);
            }
        } else {
            throw new AccessDeniedException("Forbidden");
        }
    }

}
