package com.meetingservice.DTO;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MeetingDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long meetingTypeId;
    private String meetingTypeName;
    private String status;
    private Long organizerId;
    private Set<String> participantIds;
    private Map<String, Object> details = new HashMap<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getMeetingTypeId() {
        return meetingTypeId;
    }

    public void setMeetingTypeId(Long meetingTypeId) {
        this.meetingTypeId = meetingTypeId;
    }

    public String getMeetingTypeName() {
        return meetingTypeName;
    }

    public void setMeetingTypeName(String meetingTypeName) {
        this.meetingTypeName = meetingTypeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

    public Set<String> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(Set<String> participantIds) {
        this.participantIds = participantIds;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    public MeetingDTO() {
    }

}
