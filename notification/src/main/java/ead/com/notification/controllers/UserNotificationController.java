package ead.com.notification.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ead.com.notification.configs.security.AuthenticationCurrentUserService;
import ead.com.notification.configs.security.UserDetailsImpl;
import ead.com.notification.dtos.NotificationRecordDto;
import ead.com.notification.models.NotificationModel;
import ead.com.notification.services.NotificationService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserNotificationController {

    private final NotificationService notificationService;
    private final AuthenticationCurrentUserService authenticationCurrentUserService;

    public UserNotificationController(NotificationService notificationService,
            AuthenticationCurrentUserService authenticationCurrentUserService) {
        this.notificationService = notificationService;
        this.authenticationCurrentUserService = authenticationCurrentUserService;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{userId}/notifications")
    public ResponseEntity<Page<NotificationModel>> getAllNotificationByUser(@PathVariable(value = "userId") UUID userId,
            Pageable pageable) {
        UserDetailsImpl userDetails = authenticationCurrentUserService.getCurrentUser();
        if (userDetails.getUserId().equals(userId)
                || userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(notificationService.findAllNotificationByUser(userId, pageable));

        } else {
            throw new AccessDeniedException("Forbidden");
        }
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping("/{userId}/notifications/{notificationId}")
    public ResponseEntity<NotificationModel> updateNotification(@PathVariable(value = "userId") UUID userId,
            @PathVariable(value = "notificationId") UUID notificationId,
            @RequestBody @Valid NotificationRecordDto notificationRecordDto) {
        UserDetailsImpl userDetails = authenticationCurrentUserService.getCurrentUser();
        if (userDetails.getUserId().equals(userId)
                || userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(notificationService.updateNotification(notificationRecordDto,
                            notificationService.findByNotificationIdAndUserId(notificationId, userId)));
        } else {
            throw new AccessDeniedException("Forbidden");
        }
    }
}
