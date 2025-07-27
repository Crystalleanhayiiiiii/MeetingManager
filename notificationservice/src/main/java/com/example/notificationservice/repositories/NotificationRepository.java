package com.example.notificationservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.notificationservice.models.*;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(String userId);
    List<Notification> findByUserIdAndIsReadFalse(String userId);
    List<Notification> findByMeetingId(String meetingId);
}
