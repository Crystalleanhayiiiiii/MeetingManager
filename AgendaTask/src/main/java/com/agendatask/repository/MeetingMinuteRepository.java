package com.agendatask.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agendatask.entity.MeetingMinute;

import java.util.Optional;

public interface MeetingMinuteRepository extends JpaRepository<MeetingMinute, Long> {
    Optional<MeetingMinute> findByMeetingId(String meetingId);
}
