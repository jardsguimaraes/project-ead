package com.ead.authuser.validations;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ead.authuser.dtos.UserRecordDto;
import com.ead.authuser.services.UserService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class UserValidation implements Validator {

    private final Validator validator;
    private final UserService userService;

    public UserValidation(Validator validator, UserService userService) {
        this.validator = validator;
        this.userService = userService;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(@NonNull Object object, @NonNull Errors errors) {
        var userRecordDto = (UserRecordDto) object;

        validator.validate(userRecordDto, errors);

        if (!errors.hasErrors()) {
            validateUsername(userRecordDto.username(), errors);
            validateEmail(userRecordDto.email(), errors);
        }
    }

    private void validateUsername(String userName, Errors errors) {
        if (userService.existsByUserName(userName)) {
            errors.rejectValue("username", "AuthuserNameConflict", "Authuser name is already taken");
            log.warn("Username {} is Already taken ", userName);
        }
    }

    private void validateEmail(String email, Errors errors) {
        if (userService.existsByEmail(email)) {
            errors.rejectValue("email", "EmailConflict", "Email is already taken");
            log.warn("Email {} is Already taken ", email);
        }
    }
}
