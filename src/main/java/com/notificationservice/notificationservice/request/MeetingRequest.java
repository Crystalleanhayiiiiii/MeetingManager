package com.notificationservice.notificationservice.request;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MeetingRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String meetingId;
    private String topic;
    private LocalDateTime meetingTime;
    private String organizer;
    private String location;
    private String description;

    // Default constructor
    public MeetingRequest() {
    }

    // Parameterized constructor
    public MeetingRequest(String meetingId, String topic, LocalDateTime meetingTime,
            String organizer, String location, String description) {
        this.meetingId = meetingId;
        this.topic = topic;
        this.meetingTime = meetingTime;
        this.organizer = organizer;
        this.location = location;
        this.description = description;
    }

    // Getters and Setters
    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public LocalDateTime getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(LocalDateTime meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Override toString() for better readability
    @Override
    public String toString() {
        return "MeetingRequest{" +
                "meetingId='" + meetingId + '\'' +
                ", topic='" + topic + '\'' +
                ", meetingTime=" + meetingTime +
                ", organizer='" + organizer + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}