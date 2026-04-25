package com.ead.authuser.dtos;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserCourseRecordDto(
    UUID userId,

    @NotNull(message = "CourseId is mandatory")
    UUID courseId
) {

}
