package com.notificationservice.notificationservice.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.notificationservice.notificationservice.models.Notification;
import com.notificationservice.notificationservice.repository.NotificationRepository;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationRepository repo;

    public NotificationController(NotificationRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Notification>> list(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(repo.findByUserIdOrderByCreatedAtDesc(userId, pageable));
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markRead(@PathVariable Long id) {
        Optional<Notification> n = repo.findById(id);
        if (n.isEmpty())
            return ResponseEntity.notFound().build();
        Notification notif = n.get();
        notif.setStatus("READ");
        notif.setReadAt(java.time.LocalDateTime.now());
        repo.save(notif);
        return ResponseEntity.ok().build();
    }

    // Tất cả thông báo của user
    @GetMapping("/user1/{userId}")
    public ResponseEntity<List<Notification>> listAll(@PathVariable Long userId) {
        return ResponseEntity.ok(repo.findByUserIdOrderByCreatedAtDesc(userId));
    }

    // Chưa đọc của user
    @GetMapping("/user1/{userId}/unread")
    public ResponseEntity<List<Notification>> listUnread(@PathVariable Long userId) {
        return ResponseEntity.ok(repo.findByUserIdAndStatusNotOrderByCreatedAtDesc(userId, "READ"));
    }

    // Đếm chưa đọc
    @GetMapping("/user1/{userId}/unread/count")
    public ResponseEntity<Map<String, Object>> unreadCount(@PathVariable Long userId) {
        long count = repo.countByUserIdAndStatusNot(userId, "READ");
        return ResponseEntity.ok(Map.of("userId", userId, "unread", count));
    }

    // Đánh dấu tất cả thông báo của người dùng là đã đọc
    @PostMapping("/user/{userId}/read-all")
    public ResponseEntity<Map<String, Object>> markAllRead(@PathVariable Long userId) {
        List<Notification> list = repo.findByUserIdAndStatusNotOrderByCreatedAtDesc(userId, "READ");
        int changed = 0;
        LocalDateTime now = LocalDateTime.now();
        for (Notification n : list) {
            n.setStatus("READ");
            n.setReadAt(now);
            changed++;
        }
        if (changed > 0) {
            repo.saveAll(list);
        }
        return ResponseEntity.ok(Map.of("userId", userId, "changed", changed));
    }
}
