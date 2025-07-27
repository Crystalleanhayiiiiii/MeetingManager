package com.example.notificationservice.models;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTemplate {
    @Id
    private String templateId; // e.g., "MEETING_CREATED_ORGANIZER"
    
    @Column(nullable = false)
    private String subject;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String bodyTemplate;
    
    @Column(nullable = false)
    private String notificationType; // Category of notification
}
