package ead.com.notification.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import ead.com.notification.dtos.NotificationRecordCommandDto;
import ead.com.notification.enums.NotificationStatus;
import ead.com.notification.models.NotificationModel;
import ead.com.notification.repositories.NotificationRepository;
import ead.com.notification.services.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public NotificationModel saveNotification(NotificationRecordCommandDto notificationRecordCommandDto) {
        Objects.requireNonNull(notificationRecordCommandDto, "notificationRecordCommandDto cannot be null");
        var notificationModel = new NotificationModel();

        BeanUtils.copyProperties(notificationRecordCommandDto, notificationModel);

        notificationModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        notificationModel.setNotificationStatus(NotificationStatus.CREATED);

        return notificationRepository.save(notificationModel);
    }

}
