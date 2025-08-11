package com.notificationservice.notificationservice.services;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.notificationservice.notificationservice.event.MeetingEvent;
import com.notificationservice.notificationservice.event.MeetingEventType;
//import com.notificationservice.notificationservice.models.Meeting;
import com.notificationservice.notificationservice.models.Notification;
import com.notificationservice.notificationservice.repository.NotificationRepository;

@Service
public class KafkaConsumerService {

    // @KafkaListener(topics = "my_topic", groupId = "meeting")
    // public void consume(String message) {
    // System.out.println("Message received: " + message);
    // }
    // private final NotificationRepository notificationRepository;

    // // @Autowired
    // public KafkaConsumerService(NotificationRepository notificationRepository) {
    // this.notificationRepository = notificationRepository;
    // }

    // // Kafka Listener that listens to messages from Kafka topic 'meeting-topic'
    // @KafkaListener(topics = "meeting-topic", groupId = "meeting",
    // containerFactory = "meetingKafkaListenerContainerFactory")
    // public void listenForMeeting(Meeting meeting) {
    // System.out.println("DEBUG: Received meeting: ");
    // System.out.println("DEBUG: Received meeting: " + meeting);
    // String notificationMessage = "New meeting scheduled: " + meeting.getTitle();

    // // Create a new Notification object
    // Notification notification = new Notification();
    // notification.setMessage(notificationMessage);
    // // notification.setRecipient("user@example.com"); // You can customize the
    // // recipient logic
    // // notification.setRead(false);

    // // Save the notification to the database
    // notificationRepository.save(notification);
    // System.out.println("Notification created: " + notificationMessage);
    // System.out.println("Received meeting: " + meeting);

    // }

    private final NotificationRepository notificationRepository;

    public KafkaConsumerService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @KafkaListener(topics = "meeting-topic", groupId = "notification", // dùng group riêng
            containerFactory = "meetingKafkaListenerContainerFactory")
    public void onEvent(
            com.notificationservice.notificationservice.event.MeetingEvent event,
            @org.springframework.messaging.handler.annotation.Header(name = "x-event-id", required = false) String eventId,
            @org.springframework.messaging.handler.annotation.Header(name = "x-correlation-id", required = false) String correlationId) {

        System.out.println("[Notification] Received meetingId=" + event.getMeetingId()
                + ", type=" + event.getEventType());

        java.util.Set<Long> recipients = resolveRecipients(event.getParticipantUserIds(), event.getOrganizerId());
        String message = buildMessage(event);

        for (Long uid : recipients) {
            if (uid == null)
                continue;
            Notification n = new Notification();
            n.setUserId(uid);
            n.setMeetingId(event.getMeetingId());
            n.setMessage(message);
            n.setStatus("SENT");
            n.setSentAt(java.time.LocalDateTime.now());
            // n.setEventType(event.getEventType()); // nếu entity có
            // n.setEventId(eventId); n.setCorrelationId(correlationId); // nếu có cột
            notificationRepository.save(n);
        }
    }

    // KHÔNG gắn @KafkaListener ở đây!
    private java.util.Set<Long> resolveRecipients(java.util.List<Long> participants, Long organizerId) {
        java.util.LinkedHashSet<Long> res = new java.util.LinkedHashSet<>();
        if (participants != null)
            res.addAll(participants);
        if (res.isEmpty() && organizerId != null)
            res.add(organizerId);
        return res;
    }

    private String buildMessage(com.notificationservice.notificationservice.event.MeetingEvent e) {
        String name = (e.getTitle() == null || e.getTitle().isBlank()) ? "không tên" : e.getTitle();
        switch (e.getEventType()) {
            case CREATED:
                return "Cuộc họp \"" + name + "\" vừa được tạo mới.";
            case UPDATED:
                return "Cuộc họp \"" + name + "\" vừa được cập nhật.";
            case CANCELLED:
                return "Cuộc họp \"" + name + "\" đã bị hủy.";
            default:
                return "Cuộc họp \"" + name + "\" có thay đổi.";
        }
    }
}