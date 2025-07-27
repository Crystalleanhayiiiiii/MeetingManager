package com.api.meetingservice.meetingservice.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "meetings")
@Data
@NoArgsConstructor

public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "meeting_type_id", nullable = false)
    private MeetingType meetingType;

    @Column(name = "status")
    private String status = "SCHEDULED";

    @OneToOne(mappedBy = "meeting", cascade = CascadeType.ALL)
    private OnlineMeetingDetail onlineMeetingDetail;

    @OneToOne(mappedBy = "meeting", cascade = CascadeType.ALL)
    private OfflineMeetingDetail offlineMeetingDetail;
}