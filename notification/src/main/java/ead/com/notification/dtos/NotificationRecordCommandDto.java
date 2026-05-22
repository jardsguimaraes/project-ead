package ead.com.notification.dtos;

import java.util.UUID;

public record NotificationRecordCommandDto(
    String title,
    String menssage,
    UUID userId
) {
    
}
