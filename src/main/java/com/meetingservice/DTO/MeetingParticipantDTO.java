package com.meetingservice.DTO;

import com.meetingservice.enums.ParticipantRole;
import com.meetingservice.enums.ParticipantStatus;

public class MeetingParticipantDTO {
    private Long id;
    private Long userId;
    private ParticipantRole role;
    private ParticipantStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ParticipantRole getRole() {
        return role;
    }

    public void setRole(ParticipantRole role) {
        this.role = role;
    }

    public ParticipantStatus getStatus() {
        return status;
    }

    public void setStatus(ParticipantStatus status) {
        this.status = status;
    }

    public MeetingParticipantDTO(Long id, Long userId, ParticipantRole role, ParticipantStatus status) {
        this.id = id;
        this.userId = userId;
        this.role = role;
        this.status = status;
    }

    public MeetingParticipantDTO() {
    }

}
