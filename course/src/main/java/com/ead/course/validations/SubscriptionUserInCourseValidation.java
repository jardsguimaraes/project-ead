package com.ead.course.validations;

import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ead.course.dots.SubscriptionValidationRecordDto;
import com.ead.course.enums.UserStatus;
import com.ead.course.services.CourseService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class SubscriptionUserInCourseValidation implements Validator {

    private final Validator validator;
    private final CourseService courseService;

    public SubscriptionUserInCourseValidation(Validator validator, CourseService courseService) {
        this.validator = validator;
        this.courseService = courseService;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(@NonNull Object object, @NonNull Errors errors) {
        var subscriptionValidationRecordDto = (SubscriptionValidationRecordDto) object;

        validator.validate(subscriptionValidationRecordDto, errors);

        if (!errors.hasErrors()) {
            validateExistsByCourseAndUser(subscriptionValidationRecordDto.userId(),
                    subscriptionValidationRecordDto.courseId(), errors);
            validateUserIsBlocked(subscriptionValidationRecordDto.userStatus(), errors);
        }
    }

    private void validateExistsByCourseAndUser(UUID userId, UUID courseId, Errors errors) {
        if (courseService.existsByCourseAndUser(courseId, userId)) {
            errors.reject("subscriptionConflit", "Subscription already exists!");
            log.warn("Error: Subscription already exists!", userId);
        }
    }

    private void validateUserIsBlocked(String userStatus, Errors errors) {
        if (userStatus.equals(UserStatus.BLOCKED.toString())) {
            errors.rejectValue("userId", "UserConflit", "Error: User is blocked!");
            log.warn("Error: User is blocked!");
        }
    }

}
