package com.ead.course.dots;

import java.util.UUID;

public record NotificationRecordCommandDto(
    String title,
    String menssage,
    UUID userId
) {
    
}
