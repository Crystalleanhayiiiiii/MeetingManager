package com.meetingservice.DTO;

import java.time.LocalDateTime;
import java.util.Set;

import com.meetingservice.enums.MeetingType;
import com.meetingservice.enums.OnlinePlatform;

public class CreateMeetingRequest {
    private String title;
    private String description;
    private String notes;
    private Long organizerId;
    private MeetingType type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // online
    private OnlinePlatform onlinePlatform;
    private String onlineLink;

    // offline
    private Long roomId;
    private Set<Long> deviceIds; // optional

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public OnlinePlatform getOnlinePlatform() {
        return onlinePlatform;
    }

    public void setOnlinePlatform(OnlinePlatform onlinePlatform) {
        this.onlinePlatform = onlinePlatform;
    }

    public String getOnlineLink() {
        return onlineLink;
    }

    public void setOnlineLink(String onlineLink) {
        this.onlineLink = onlineLink;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Set<Long> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(Set<Long> deviceIds) {
        this.deviceIds = deviceIds;
    }

    public CreateMeetingRequest() {
    }

}
