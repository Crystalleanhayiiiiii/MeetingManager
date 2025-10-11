// com.meetingservice.services.MeetingService.java
package com.meetingservice.services;

import com.meetingservice.DTO.*;
import com.meetingservice.enums.*;
//import com.meetingservice.services.MeetingProducerService;
import com.meetingservice.models.Meeting;
import com.meetingservice.models.MeetingParticipant;

import com.meetingservice.repository.MeetingParticipantRepository;
import com.meetingservice.repository.MeetingRepository;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeetingService {

    private final MeetingRepository meetingRepo;
    private final MeetingParticipantRepository participantRepo;

    public MeetingService(MeetingRepository meetingRepo, MeetingParticipantRepository participantRepo) {
        this.meetingRepo = meetingRepo;
        this.participantRepo = participantRepo;
    }

    // === CRUD MEETING ===
    public Meeting createMeeting(Meeting meeting) {
        meeting.setStatus(MeetingStatus.UPCOMING);
        return meetingRepo.save(meeting);
    }

    public Meeting updateMeeting(Long id, Meeting updated) {
        return meetingRepo.findById(id).map(m -> {
            m.setName(updated.getName());
            m.setDescription(updated.getDescription());
            m.setStartTime(updated.getStartTime());
            m.setEndTime(updated.getEndTime());
            m.setRoomId(updated.getRoomId());
            return meetingRepo.save(m);
        }).orElseThrow(() -> new RuntimeException("Meeting not found"));
    }

    public void cancelMeeting(Long id, String reason) {
        Meeting m = meetingRepo.findById(id).orElseThrow(() -> new RuntimeException("Meeting not found"));
        m.setStatus(MeetingStatus.CANCELLED);
        m.setCancelReason(reason);
        meetingRepo.save(m);
    }

    public void deleteMeeting(Long id) {
        meetingRepo.deleteById(id);
    }

    public List<Meeting> getAllMeetings() {
        return meetingRepo.findAll();
    }

    public Optional<Meeting> getMeetingById(Long id) {
        return meetingRepo.findById(id);
    }

    public List<Meeting> getMeetingsByStatus(MeetingStatus status) {
        return meetingRepo.findByStatus(status);
    }

    public List<Meeting> getMeetingsBetween(LocalDateTime start, LocalDateTime end) {
        return meetingRepo.findMeetingsBetween(start, end);
    }

    // === PARTICIPANTS ===
    public MeetingParticipant addParticipant(Long meetingId, MeetingParticipant p) {
        Meeting meeting = meetingRepo.findById(meetingId).orElseThrow(() -> new RuntimeException("Meeting not found"));
        p.setMeeting(meeting);
        return participantRepo.save(p);
    }

    public void removeParticipant(Long participantId) {
        participantRepo.deleteById(participantId);
    }

    public List<MeetingParticipant> getParticipants(Long meetingId) {
        return participantRepo.findByMeetingId(meetingId);
    }

    public void updateParticipantStatus(Long meetingId, Long userId, ParticipantStatus status) {
        MeetingParticipant p = participantRepo.findByMeetingAndUser(meetingId, userId);
        if (p != null) {
            p.setStatus(status);
            participantRepo.save(p);
        }
    }

    // ✅ Helper: convert list<Meeting> -> list<MeetingDTO>
    public List<MeetingDTO> convertToDTOs(List<Meeting> meetings) {
        return meetings.stream().map(MeetingDTO::new).collect(Collectors.toList());
    }
}
