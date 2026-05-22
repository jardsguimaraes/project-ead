package ead.com.notification.services;

import ead.com.notification.dtos.NotificationRecordCommandDto;
import ead.com.notification.models.NotificationModel;

public interface NotificationService {

    NotificationModel saveNotification(NotificationRecordCommandDto notificationRecordCommandDto);
    
}
