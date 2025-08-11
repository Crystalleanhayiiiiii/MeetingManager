// package com.notificationservice.notificationservice.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;

// import
// com.notificationservice.notificationservice.services.KafkaConsumerService;

// public class KafkaController {
// private final KafkaConsumerService kafkaConsumerService;

// @Autowired
// public KafkaController(KafkaController kafkaConsumerService) {
// this.kafkaConsumerService = kafkaConsumerService;
// }

// // Endpoint to retrieve all notifications
// @GetMapping("/notifications")
// public List<Notification> getAllNotifications() {
// return notificationService.getAllNotifications(); // Return all notifications
// }

// // Endpoint to retrieve a specific notification by its ID
// @GetMapping("/notifications/{id}")
// public Notification getNotificationById(@PathVariable Long id) {
// Notification notification = notificationService.getNotificationById(id);
// if (notification == null) {
// throw new RuntimeException("Notification not found with id: " + id); //
// Handle not found error
// }
// return notification;
// }
// }
