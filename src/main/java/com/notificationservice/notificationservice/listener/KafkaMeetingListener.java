// package com.notificationservice.notificationservice.listener;

// import org.springframework.kafka.annotation.KafkaListener;
// import org.springframework.stereotype.Service;

// import com.notificationservice.notificationservice.request.MeetingRequest;

// @Service
// public class KafkaMeetingListener {

// @KafkaListener(topics = "meeting-topic", groupId = "notification-group",
// containerFactory = "meetingKafkaListenerContainerFactory")
// public void listenMeetingRequest(MeetingRequest meetingRequest) {
// // Process the MeetingRequest object
// System.out.println("Received MeetingRequest: " + meetingRequest);
// // You can perform further actions here like sending notifications, saving to
// // DB, etc.
// }
// }
