package ead.com.notification.services.impl;

import ead.com.notification.repositories.NotificationRepository;
import ead.com.notification.services.NotificationService;

public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

}
