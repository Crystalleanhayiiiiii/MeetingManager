package com.example.notificationservice.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.notificationservice.models.Notification;
import com.example.notificationservice.models.NotificationTemplate;
import com.example.notificationservice.repositories.NotificationRepository;
import com.example.notificationservice.repositories.NotificationTemplateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationTemplateRepository templateRepository;
    private final EmailService emailService;
    private final TemplateEngine templateEngine;

    public void createAndSendNotification(String userId, String notificationType, 
                                        String meetingId, Map<String, Object> variables) {
        // Determine recipient type (organizer or participant)
        String recipientType = getRecipientType(userId, variables);
        
        // Find appropriate template
        NotificationTemplate template = templateRepository.findByTemplateId(notificationType + "_" + recipientType)
                .orElseGet(() -> templateRepository.findByTemplateId("DEFAULT_" + notificationType)
                .orElseThrow(() -> new RuntimeException("No template found for type: " + notificationType));

        // Process template with variables
        Context context = new Context();
        context.setVariables(variables);
        String content = templateEngine.process(template.getBodyTemplate(), context);

        // Create and save notification
        Notification notification = Notification.builder()
                .userId(userId)
                .type(notificationType)
                .content(content)
                .sentAt(LocalDateTime.now())
                .meetingId(meetingId)
                .isRead(false)
                .build();
        
        notificationRepository.save(notification);

        // Send email if recipient has email address
        if (variables.containsKey("email")) {
            emailService.sendEmail(
                (String) variables.get("email"),
                template.getSubject(),
                content
            );
        }
    }

    public List<Notification> getUserNotifications(String userId) {
        return notificationRepository.findByUserId(userId);
    }

    public List<Notification> getUnreadNotifications(String userId) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId);
    }

    public Notification markAsRead(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .map(notification -> {
                    notification.setRead(true);
                    return notificationRepository.save(notification);
                })
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));
    }

    private String getRecipientType(String userId, Map<String, Object> variables) {
        String organizerId = (String) variables.get("organizerId");
        return userId.equals(organizerId) ? "ORGANIZER" : "PARTICIPANT";
    }
}