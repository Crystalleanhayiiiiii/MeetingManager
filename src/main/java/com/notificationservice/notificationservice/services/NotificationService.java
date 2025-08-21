package com.notificationservice.notificationservice.services;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;

import org.springframework.stereotype.Service;

import com.notificationservice.notificationservice.enums.MeetingStatus;
import com.notificationservice.notificationservice.enums.MeetingType;
import com.notificationservice.notificationservice.event.MeetingEvent;
import com.notificationservice.notificationservice.models.Notification;
import com.notificationservice.notificationservice.repository.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository repo;

    public NotificationService(NotificationRepository repo) {
        this.repo = repo;
    }

    public void handle(MeetingEvent ev, String eventId, String correlationId) {
        if (ev == null)
            return;

        // recipients = participants; nếu rỗng thì organizer
        LinkedHashSet<Long> recipients = new LinkedHashSet<>();
        if (ev.getParticipantUserIds() != null)
            recipients.addAll(ev.getParticipantUserIds());
        if (recipients.isEmpty() && ev.getOrganizerId() != null)
            recipients.add(ev.getOrganizerId());

        String title = titleOf(ev);
        String message = messageOf(ev);

        // Gửi thông báo cho từng người (người được mời hoặc người tổ chức)
        for (Long uid : recipients) {
            if (uid == null)
                continue;

            // idempotent: tránh gửi lại thông báo cho cùng một sự kiện (nếu header có
            // eventId)
            if (eventId != null && repo.existsByUserIdAndEventId(uid, eventId))
                continue;

            Notification n = new Notification();
            n.setUserId(uid);
            n.setMeetingId(ev.getMeetingId());
            n.setEventType(ev.getEventType());
            n.setCategory("MEETING");
            n.setTitle(title);
            n.setMessage(message);
            n.setEventId(eventId);
            n.setCorrelationId(correlationId);
            n.setStatus("SENT");
            n.setSentAt(LocalDateTime.now());
            repo.save(n);
        }
    }

    private String titleOf(MeetingEvent ev) {
        switch (ev.getEventType()) {
            case CREATED:
                return "Cuộc họp mới";
            case UPDATED:
                if (ev.getType() == MeetingType.OFFLINE && ev.getStatus() == MeetingStatus.UPCOMING)
                    return "Cuộc họp đã được duyệt";
                return "Cuộc họp cập nhật";
            case CANCELLED:
                return "Cuộc họp bị hủy";
            case ADDED_PARTICIPANT:
                return "Bạn được mời tham gia cuộc họp";
            default:
                return "Thông báo cuộc họp";
        }
    }

    private String messageOf(MeetingEvent ev) {
        String name = ev.getTitle() == null || ev.getTitle().isBlank() ? "không tên" : ev.getTitle();
        switch (ev.getEventType()) {
            case CREATED:
                return "Cuộc họp \"" + name + "\" vừa được tạo mới.";
            case UPDATED:
                return "Cuộc họp \"" + name + "\" vừa được cập nhật.";
            case CANCELLED:
                return "Cuộc họp \"" + name + "\" đã bị hủy.";
            case ADDED_PARTICIPANT:
                return "Bạn đã được mời tham gia cuộc họp \"" + name + "\".";
            default:
                return "Cuộc họp \"" + name + "\" có thay đổi.";
        }
    }
}
