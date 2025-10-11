package com.meetingservice.DTO;

import java.time.LocalDateTime;

import java.util.List;

import com.meetingservice.enums.MeetingStatus;

public class MeetingDTO {
    private Long id;
    private String name;
    private String description;
    private MeetingStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long roomId;
    private String cancelReason;
    private List<MeetingParticipantDTO> participants;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public List<MeetingParticipantDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(List<MeetingParticipantDTO> participants) {
        this.participants = participants;
    }

    public MeetingDTO(Long id, String name, String description, MeetingStatus status, LocalDateTime startTime,
            LocalDateTime endTime, Long roomId, String cancelReason, List<MeetingParticipantDTO> participants) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomId = roomId;
        this.cancelReason = cancelReason;
        this.participants = participants;
    }

    public MeetingDTO() {
    }

}
