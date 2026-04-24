package com.ead.course.dots;

import java.util.UUID;

import com.ead.course.enums.UserStatus;
import com.ead.course.enums.UserType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserRecordDto(
                UUID userId,
                String username,
                String email,
                String fullName,
                UserStatus userStatus,
                UserType userType,
                String phoneNumber,
                String imageUrl) {

}
