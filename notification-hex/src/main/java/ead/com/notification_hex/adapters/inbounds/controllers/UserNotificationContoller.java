package ead.com.notification_hex.adapters.inbounds.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

import ead.com.notification_hex.adapters.configs.security.AuthenticationCurrentUserService;
import ead.com.notification_hex.adapters.configs.security.UserDetailsImpl;
import ead.com.notification_hex.adapters.dots.NotificationRecordDto;
import ead.com.notification_hex.core.domain.NotificationDomain;
import ead.com.notification_hex.core.domain.PageInfo;
import ead.com.notification_hex.core.ports.NotificationServicePort;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserNotificationContoller {

    private final NotificationServicePort notificationServicePort;
    private final AuthenticationCurrentUserService authenticationCurrentUserService;

    public UserNotificationContoller(NotificationServicePort notificationServicePort,
            AuthenticationCurrentUserService authenticationCurrentUserService) {
        this.notificationServicePort = notificationServicePort;
        this.authenticationCurrentUserService = authenticationCurrentUserService;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{userId}/notifications")
    public ResponseEntity<Page<NotificationDomain>> getAllNotificationByUser(
            @PathVariable(value = "userId") UUID userId,
            Pageable pageable) {
        UserDetailsImpl userDetails = authenticationCurrentUserService.getCurrentUser();

        if (userDetails.getUserId().equals(userId)
                || userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            var pageInfo = new PageInfo();
            BeanUtils.copyProperties(pageable, pageInfo);

            List<NotificationDomain> notificationDomainList = notificationServicePort.findAllNotificationByUser(userId,
                    pageInfo);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new PageImpl<>(notificationDomainList, pageable, notificationDomainList.size()));

        } else {
            throw new AccessDeniedException("Forbidden");
        }
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping("/{userId}/notifications/{notificationId}")
    public ResponseEntity<NotificationDomain> updateNotification(@PathVariable(value = "userId") UUID userId,
            @PathVariable(value = "notificationId") UUID notificationId,
            @RequestBody @Valid NotificationRecordDto notificationRecordDto) {
        UserDetailsImpl userDetails = authenticationCurrentUserService.getCurrentUser();

        if (userDetails.getUserId().equals(userId)
                || userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(notificationServicePort.updateNotification(notificationRecordDto.notificationStatus(),
                            notificationServicePort.findByNotificationIdAndUserId(notificationId, userId)));
        } else {
            throw new AccessDeniedException("Forbidden");
        }
    }
}
