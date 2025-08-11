package com.meetingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meetingservice.models.MeetingParticipant;

public interface MeetingParticipantRepository extends JpaRepository<MeetingParticipant, Long> {
    List<MeetingParticipant> findByMeetingId(Long meetingId);

    boolean existsByMeetingIdAndUserId(Long meetingId, Long userId);

    // void save(MeetingParticipant organizer);
}
