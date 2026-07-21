package ead.com.notification_hex.adapters.dots;

import java.util.UUID;

public record NotificationRecordCommandDto(
        String title,
        String message,
        UUID userId) {

}
