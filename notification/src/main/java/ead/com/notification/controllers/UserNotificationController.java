package ead.com.notification.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ead.com.notification.dtos.NotificationRecordDto;
import ead.com.notification.models.NotificationModel;
import ead.com.notification.services.NotificationService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserNotificationController {

    private final NotificationService notificationService;

    public UserNotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{userId}/notifications")
    public ResponseEntity<Page<NotificationModel>> getAllNotificationByUser(@PathVariable(value = "userId") UUID userId,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(notificationService.findAllNotificationByUser(userId, pageable));
    }

    @PutMapping("/{userId}/notifications/{notificationId}")
    public ResponseEntity<NotificationModel> updateNotification(@PathVariable(value = "userId") UUID userId,
            @PathVariable(value = "notificationId") UUID notificationId,
            @RequestBody @Valid NotificationRecordDto notificationRecordDto) {
        return ResponseEntity.status(HttpStatus.OK).body(notificationService.updateNotification(notificationRecordDto,
                notificationService.findByNotificationIdAndUserId(notificationId, userId)));
    }
}
