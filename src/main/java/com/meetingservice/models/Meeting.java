package com.meetingservice.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.meetingservice.enums.MeetingStatus;
import com.meetingservice.enums.MeetingType;
import com.meetingservice.enums.OnlinePlatform;

import jakarta.persistence.CascadeType;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.OneToMany;

import jakarta.persistence.Table;

@Entity
@Table(name = "meetings")

public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(length = 2000)
    private String description;

    private Long organizerId;

    @Enumerated(EnumType.STRING)
    private MeetingType type;

    @Enumerated(EnumType.STRING)
    private MeetingStatus status;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // ONLINE fields
    @Enumerated(EnumType.STRING)
    private OnlinePlatform onlinePlatform;
    private String onlineLink;

    // OFFLINE fields
    private Long roomId;

    // simple device ids (nếu sau này cần entity Device thì tách)
    private Set<Long> deviceIds = new HashSet<>();
    // participants
    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MeetingParticipant> participants = new ArrayList<>();

    // optional: lưu lý do hủy
    private String cancelReason;

    public Meeting() {
    }

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

    public List<MeetingParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<MeetingParticipant> participants) {
        this.participants = participants;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Set<Long> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(Set<Long> deviceIds) {
        this.deviceIds = (deviceIds == null) ? new HashSet<>() : new HashSet<>(deviceIds);
    }

    // Patch an toàn: nếu ai đó gọi setDeviceId(single) thì ta thêm vào set thay vì
    // throw
    public void setDeviceId(Long deviceId) {
        if (this.deviceIds == null)
            this.deviceIds = new HashSet<>();
        if (deviceId != null)
            this.deviceIds.add(deviceId);

    }
}
