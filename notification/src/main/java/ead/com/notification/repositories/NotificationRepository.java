package ead.com.notification.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ead.com.notification.models.NotificationModel;

public interface NotificationRepository extends JpaRepository<NotificationModel, UUID> {
    
}
