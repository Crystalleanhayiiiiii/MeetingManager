package com.meetingservice.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.meetingservice.repository.MeetingRepository;
import com.meetingservice.enums.MeetingStatus;
import com.meetingservice.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Component
public class MeetingEventListener {

    private static final Logger logger = LoggerFactory.getLogger(MeetingEventListener.class);

    private final MeetingRepository meetingRepository;

    public MeetingEventListener(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    @KafkaListener(topics = "meeting.status.update", groupId = "meeting-service")
    public void onMeetingStatusUpdate(ConsumerRecord<String, Map<String, Object>> record) {
        Map<String, Object> payload = record.value();

        Integer meetingId = (Integer) payload.get("meetingId");
        String status = (String) payload.get("status");
        String reason = (String) payload.get("reason");

        logger.info("📥 Received meeting status update: meetingId={} status={} reason={}", meetingId, status);

        if (meetingId != null && status != null) {
            Long meetingIdLong = Long.valueOf(meetingId);
            // Cập nhật trạng thái của Meeting
            Meeting meeting = meetingRepository.findById(meetingIdLong)
                    .orElseThrow(() -> new IllegalArgumentException("Meeting not found"));
            MeetingStatus meetingStatus = convertStringToMeetingStatus(status);
            meeting.setStatus(meetingStatus);
            meeting.setCancelReason(reason);
            meetingRepository.save(meeting);

            logger.info("📦 Meeting status updated to {} for meetingId={}", status, meetingId);
        } else {
            logger.error("❌ Missing meetingId or status in payload: {}", payload);
        }
    }

    // Hàm chuyển đổi trạng thái từ RoomService sang MeetingService
    private MeetingStatus convertRoomStatusToMeetingStatus(String roomStatus) {
        switch (roomStatus) {
            case "PENDING":
                return MeetingStatus.PENDING_APPROVAL;
            case "CONFIRMED":
                return MeetingStatus.COMPLETED;
            case "CANCELLED":
                return MeetingStatus.CANCELLED;
            default:
                throw new IllegalArgumentException("Invalid RoomService status: " + roomStatus);
        }
    }

    private MeetingStatus convertStringToMeetingStatus(String status) {
        try {
            return MeetingStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status); // Trường hợp không hợp lệ
        }
    }
}