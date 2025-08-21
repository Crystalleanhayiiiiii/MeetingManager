package com.notificationservice.notificationservice.repository;

import com.notificationservice.notificationservice.models.Notification;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, org.springframework.data.domain.Pageable pageable);

    boolean existsByUserIdAndEventId(Long userId, String eventId);

    // Tất cả thông báo của user, mới nhất trước
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Chưa đọc (status != READ)
    List<Notification> findByUserIdAndStatusNotOrderByCreatedAtDesc(Long userId, String status);

    long countByUserIdAndStatusNot(Long userId, String status);
}