package com.meetingservice.event;

import java.time.LocalDateTime;
import java.util.List;

import com.meetingservice.enums.MeetingStatus;
import com.meetingservice.enums.MeetingType;
import com.meetingservice.enums.OnlinePlatform;

import lombok.*;

@AllArgsConstructor
public class MeetingEvent {
    private MeetingEventType eventType;
    private Long meetingId;
    private String title;
    private Long organizerId;
    private MeetingType type;
    private MeetingStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // online/offline summary
    private String onlineLink;
    private OnlinePlatform onlinePlatform;
    private Long roomId;

    // participants (ids) – để Notification biết gửi ai
    private List<Long> participantUserIds;

    // optional
    private String cancelReason;
    private LocalDateTime eventTime;

    public MeetingEvent() {
    }

    public MeetingEventType getEventType() {
        return eventType;
    }

    public void setEventType(MeetingEventType eventType) {
        this.eventType = eventType;
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

    public MeetingType getType() {
        return type;
    }

    public void setType(MeetingType type) {
        this.type = type;
    }

    public MeetingStatus getStatus() {
        return status;
    }

    public void setStatus(MeetingStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getOnlineLink() {
        return onlineLink;
    }

    public void setOnlineLink(String onlineLink) {
        this.onlineLink = onlineLink;
    }

    public OnlinePlatform getOnlinePlatform() {
        return onlinePlatform;
    }

    public void setOnlinePlatform(OnlinePlatform onlinePlatform) {
        this.onlinePlatform = onlinePlatform;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public List<Long> getParticipantUserIds() {
        return participantUserIds;
    }

    public void setParticipantUserIds(List<Long> participantUserIds) {
        this.participantUserIds = participantUserIds;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

}
