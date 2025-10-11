package com.meetingservice.repository;

import com.meetingservice.enums.MeetingStatus;
import com.meetingservice.models.Meeting;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {

  List<Meeting> findByStatus(MeetingStatus status);

  @Query("SELECT m FROM Meeting m WHERE m.startTime BETWEEN :start AND :end")
  List<Meeting> findMeetingsBetween(LocalDateTime start, LocalDateTime end);

}