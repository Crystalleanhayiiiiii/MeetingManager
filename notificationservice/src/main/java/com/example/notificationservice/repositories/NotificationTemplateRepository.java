package com.example.notificationservice.repositories;

import com.example.notificationservice.models.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, String> {
    List<NotificationTemplate> findByNotificationType(String notificationType);
    Optional<NotificationTemplate> findByTemplateId(String templateId);
    List<NotificationTemplate> findByNotificationTypeAndRecipientType(String notificationType, String recipientType);
}