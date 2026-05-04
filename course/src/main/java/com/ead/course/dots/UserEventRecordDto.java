package com.ead.course.dots;

import java.util.UUID;

import org.springframework.beans.BeanUtils;

import com.ead.course.models.UserModel;

public record UserEventRecordDto(
    UUID userId,
    String username,
    String email,
    String fullName,
    String userType,
    String userStatus,
    String phoneNumber,
    String imageUrl,
    String actionType
) {

    public UserModel convertToUserModel() {
        var userModel = new UserModel();
        BeanUtils.copyProperties(this, userModel);
        return userModel;
    }
}
