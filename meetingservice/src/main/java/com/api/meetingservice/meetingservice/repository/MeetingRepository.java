// MeetingRepository.java
package com.api.meetingservice.meetingservice.repository;

import com.api.meetingservice.meetingservice.models.Meeting;
import com.api.meetingservice.meetingservice.models.MeetingType;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByMeetingType(MeetingType meetingType);

    List<Meeting> findByStartTimeBetween(LocalDateTime now, LocalDateTime next24Hours);
}