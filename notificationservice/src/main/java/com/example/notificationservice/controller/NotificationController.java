package com.example.notificationservice.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.notificationservice.models.Notification;
import com.example.notificationservice.repositories.NotificationRepository;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender javaMailSender;

    public NotificationController(NotificationRepository notificationRepository, 
                                JavaMailSender javaMailSender) {
        this.notificationRepository = notificationRepository;
        this.javaMailSender = javaMailSender;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(
            @PathVariable String userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(
            @PathVariable String userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndIsReadFalse(userId);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{id}/mark-as-read")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long id) {
        return notificationRepository.findById(id)
                .map(notification -> {
                    notification.setRead(true);
                    notificationRepository.save(notification);
                    return ResponseEntity.ok(notification);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/meeting/{meetingId}")
    public ResponseEntity<List<Notification>> getMeetingNotifications(
            @PathVariable String meetingId) {
        List<Notification> notifications = notificationRepository.findByMeetingId(meetingId);
        return ResponseEntity.ok(notifications);
    }

   @GetMapping("/test-email")
public ResponseEntity<String> testEmail() {
    try {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("danghang2411@gmail.com"); // Must match spring.mail.username
        message.setTo("hanghangha003@gmail.com"); 
        message.setSubject("Test Email");
        message.setText("This is working now!");
        
        javaMailSender.send(message);
        return ResponseEntity.ok("Email sent successfully");
    } catch (Exception e) {
        return ResponseEntity.internalServerError()
            .body("Failed to send email: " + e.getCause().getMessage());
    }
}
}