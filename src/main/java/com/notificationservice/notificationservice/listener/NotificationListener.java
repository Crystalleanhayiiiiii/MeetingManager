// // package com.notificationservice.notificationservice.listener;

// // import org.springframework.kafka.annotation.KafkaListener;
// // import org.springframework.stereotype.Service;

// // import com.notificationservice.notificationservice.models.Notification;
// // import
// com.notificationservice.notificationservice.repository.NotificationRepository;

// // @Service
// // public class NotificationListener {

// // private final NotificationRepository notificationRepository;

// // public NotificationListener(NotificationRepository notificationRepository)
// {
// // this.notificationRepository = notificationRepository;
// // }

// // @KafkaListener(topics = "createmeeting", groupId = "notification-group")
// // public void listenForMeetingCreation(String message) {
// // System.out.println("Received message: " + message); // Log thông điệp nhận
// được
// // // Xử lý thông điệp, ví dụ lưu thông báo vào cơ sở dữ liệu
// // Notification notification = new Notification();
// // notification.setMessage("A new meeting has been created: " + message);
// // notificationRepository.save(notification);
// // System.out.println("Notification saved: " + notification); // Log khi
// thông báo đã được lưu
// // }

// // }
// package com.notificationservice.notificationservice.listener;

// import org.springframework.kafka.annotation.KafkaListener;
// import org.springframework.stereotype.Component;

// import com.notificationservice.notificationservice.models.Notification;
// import
// com.notificationservice.notificationservice.repository.NotificationRepository;

// import lombok.extern.slf4j.Slf4j;

// // import com.notificationservice.notificationservice.models.Notification;
// // import
// com.notificationservice.notificationservice.repository.NotificationRepository;
// // import org.springframework.kafka.annotation.KafkaListener;
// // import org.springframework.stereotype.Component;

// // @Component
// // public class NotificationListener {

// // private final NotificationRepository notificationRepository;

// // public NotificationListener(NotificationRepository notificationRepository)
// {
// // this.notificationRepository = notificationRepository;
// // }

// // @KafkaListener(topics = "notifications", groupId = "notification-group")
// // public void listenNotificationTopic(String message) {
// // System.out.println("Received Message: " + message);

// // // Tạo và lưu thông báo vào database
// // Notification notification = new Notification();
// // notification.setMessage(message);
// // notificationRepository.save(notification);

// // System.out.println("Notification saved to database: " + message);
// // }
// // }

// @Component
// @Slf4j // Sử dụng Lombok để log
// public class NotificationListener {

// private final NotificationRepository notificationRepository;

// public NotificationListener(NotificationRepository notificationRepository) {
// this.notificationRepository = notificationRepository;
// }

// @KafkaListener(topics = "notifications", groupId = "notification-group")
// public void listenNotificationTopic(Notification notification) {
// log.info("Received Notification: {}", notification);
// notificationRepository.save(notification);
// log.info("Saved to DB: {}", notification);
// }
// }
