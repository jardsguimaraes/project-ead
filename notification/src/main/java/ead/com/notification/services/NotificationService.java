package ead.com.notification.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ead.com.notification.dtos.NotificationRecordCommandDto;
import ead.com.notification.dtos.NotificationRecordDto;
import ead.com.notification.models.NotificationModel;

public interface NotificationService {

    NotificationModel saveNotification(NotificationRecordCommandDto notificationRecordCommandDto);

    Page<NotificationModel> findAllNotificationByUser(UUID userId, Pageable pageable);

    NotificationModel findByNotificationIdAndUserId(UUID notificationId, UUID userId);

    NotificationModel updateNotification(NotificationRecordDto notificationRecordDto,
            NotificationModel notificationModel);

}
