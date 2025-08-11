package com.meetingservice.repository;

import com.meetingservice.enums.MeetingStatus;
import com.meetingservice.models.Meeting;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {

        // UPCOMING cho user (vừa organizer vừa participant)
        @Query("""
                        select distinct m from Meeting m
                        left join m.participants p
                        where (m.organizerId = :userId or p.userId = :userId)
                          and m.status = :status
                          and m.startTime > :now
                        """)
        Page<Meeting> findUpcomingForUser(@Param("userId") Long userId,
                        @Param("status") MeetingStatus status,
                        @Param("now") LocalDateTime now,
                        Pageable pageable);

        // UPCOMING chỉ organizer
        @Query("""
                        select m from Meeting m
                        where m.organizerId = :userId
                          and m.status = :status
                          and m.startTime > :now
                        """)
        Page<Meeting> findUpcomingOrganizer(@Param("userId") Long userId,
                        @Param("status") MeetingStatus status,
                        @Param("now") LocalDateTime now,
                        Pageable pageable);

        // UPCOMING chỉ attendee (khác organizer)
        @Query("""
                        select distinct m from Meeting m
                        join m.participants p
                        where p.userId = :userId
                          and m.organizerId <> :userId
                          and m.status = :status
                          and m.startTime > :now
                        """)
        Page<Meeting> findUpcomingAttendee(@Param("userId") Long userId,
                        @Param("status") MeetingStatus status,
                        @Param("now") LocalDateTime now,
                        Pageable pageable);

        // COMPLETED (hoặc đã kết thúc theo thời gian) cho user (cả 2 vai)
        @Query("""
                        select distinct m from Meeting m
                        left join m.participants p
                        where (m.organizerId = :userId or p.userId = :userId)
                          and (m.status = :completed or m.endTime < :now)
                        """)
        Page<Meeting> findCompletedForUser(@Param("userId") Long userId,
                        @Param("completed") MeetingStatus completed,
                        @Param("now") LocalDateTime now,
                        Pageable pageable);

        @Query("""
                        select distinct m from Meeting m
                        left join m.participants p
                        where (m.organizerId = :userId or p.userId = :userId)
                        order by m.startTime desc
                        """)
        List<Meeting> findAllMeetingForUser(Long userId);
        
}