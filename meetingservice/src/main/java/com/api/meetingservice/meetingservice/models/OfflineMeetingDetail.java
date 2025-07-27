// OfflineMeetingDetail.java
package com.api.meetingservice.meetingservice.models;

import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(name = "online_meeting_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfflineMeetingDetail {
    @Id
    @Column(name = "meeting_id")
    private Long meetingId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @Column(name = "room_location")
    private String roomLocation;

    @Column(name = "equipment_needed")
    private String equipmentNeeded;

    @Column(name = "quantity")
    private Integer quantity;
}