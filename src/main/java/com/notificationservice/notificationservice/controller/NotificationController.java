package com.notificationservice.notificationservice.controller;

import java.util.Optional;

import org.springframework.data.domain.Sort;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
}
