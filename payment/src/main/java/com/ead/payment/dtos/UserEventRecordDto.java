package com.ead.payment.dtos;

import java.util.UUID;

import org.springframework.beans.BeanUtils;

import com.ead.payment.models.UserModel;

public record UserEventRecordDto(
        UUID userId,
        String username,
        String email,
        String fullName,
        String userStatus,
        String userType,
        String phoneNumber,
        String imageUrl,
        String actionType) {

    public UserModel convertToUserModel() {
        var userModel = new UserModel();
        BeanUtils.copyProperties(this, userModel);
        return userModel;
    }

}
