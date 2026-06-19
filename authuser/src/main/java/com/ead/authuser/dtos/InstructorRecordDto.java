package com.ead.authuser.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record InstructorRecordDto(
                @NotNull(message = "UserId is mandatory!") UUID userId) {

}
