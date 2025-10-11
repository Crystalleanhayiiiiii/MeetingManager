package com.meetingservice.controller;

import java.time.LocalDateTime;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.meetingservice.enums.MeetingStatus;
import com.meetingservice.enums.ParticipantStatus;
import com.meetingservice.models.Meeting;
import com.meetingservice.models.MeetingParticipant;
import com.meetingservice.services.*;

@RestController
@RequestMapping("/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    // === MEETING CRUD ===
    @PostMapping
    public Meeting createMeeting(@RequestBody Meeting meeting) {
        return meetingService.createMeeting(meeting);
    }

    @PutMapping("/{id}")
    public Meeting updateMeeting(@PathVariable Long id, @RequestBody Meeting meeting) {
        return meetingService.updateMeeting(id, meeting);
    }

    @PutMapping("/{id}/cancel")
    public void cancelMeeting(@PathVariable Long id, @RequestParam String reason) {
        meetingService.cancelMeeting(id, reason);
    }

    @DeleteMapping("/{id}")
    public void deleteMeeting(@PathVariable Long id) {
        meetingService.deleteMeeting(id);
    }

    @GetMapping
    public List<Meeting> getAllMeetings() {
        return meetingService.getAllMeetings();
    }

    @GetMapping("/{id}")
    public Meeting getMeeting(@PathVariable Long id) {
        return meetingService.getMeetingById(id).orElseThrow(() -> new RuntimeException("Meeting not found"));
    }

    @GetMapping("/status/{status}")
    public List<Meeting> getMeetingsByStatus(@PathVariable MeetingStatus status) {
        return meetingService.getMeetingsByStatus(status);
    }

    @GetMapping("/between")
    public List<Meeting> getMeetingsBetween(@RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return meetingService.getMeetingsBetween(start, end);
    }

    // === PARTICIPANT ===
    @PostMapping("/{meetingId}/participants")
    public MeetingParticipant addParticipant(@PathVariable Long meetingId,
            @RequestBody MeetingParticipant participant) {
        return meetingService.addParticipant(meetingId, participant);
    }

    @GetMapping("/{meetingId}/participants")
    public List<MeetingParticipant> getParticipants(@PathVariable Long meetingId) {
        return meetingService.getParticipants(meetingId);
    }

    @DeleteMapping("/participants/{participantId}")
    public void removeParticipant(@PathVariable Long participantId) {
        meetingService.removeParticipant(participantId);
    }

    @PutMapping("/{meetingId}/participants/{userId}/status")
    public void updateParticipantStatus(@PathVariable Long meetingId,
            @PathVariable Long userId,
            @RequestParam ParticipantStatus status) {
        meetingService.updateParticipantStatus(meetingId, userId, status);
    }
}
