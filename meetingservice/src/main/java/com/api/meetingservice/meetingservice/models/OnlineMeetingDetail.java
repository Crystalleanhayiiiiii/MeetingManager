// OnlineMeetingDetail.java
package com.api.meetingservice.meetingservice.models;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "online_meeting_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnlineMeetingDetail {
    @Id
    @Column(name = "meeting_id")
    private Long meetingId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @Column(name = "online_link")
    private String onlineLink;

    @Column(name = "platform")
    private String platform;
}