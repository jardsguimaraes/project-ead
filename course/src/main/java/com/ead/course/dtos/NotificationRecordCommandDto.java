package com.ead.course.dtos;

import java.util.UUID;

public record NotificationRecordCommandDto(
    String title,
    String menssage,
    UUID userId
) {
    
}
