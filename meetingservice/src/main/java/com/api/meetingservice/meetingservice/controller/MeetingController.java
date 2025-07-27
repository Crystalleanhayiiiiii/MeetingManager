package com.api.meetingservice.meetingservice.controller;


import com.api.meetingservice.meetingservice.DTO.MeetingDTO;

import com.api.meetingservice.meetingservice.models.Meeting;
import com.api.meetingservice.meetingservice.services.MeetingService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api/meetings")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @PostMapping
    public ResponseEntity<Meeting> createMeeting(@Valid @RequestBody MeetingDTO meetingDTO) {
        Meeting createdMeeting = meetingService.createMeeting(meetingDTO);
        return ResponseEntity.ok(createdMeeting);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Meeting> updateMeeting(@PathVariable Long id, @RequestBody MeetingDTO meetingDTO) {
        Meeting updatedMeeting = meetingService.updateMeeting(id, meetingDTO);
        return ResponseEntity.ok(updatedMeeting);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meeting> getMeeting(@PathVariable Long id) {
        return meetingService.getMeetingById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Meeting>> getAllMeetings() {
        return ResponseEntity.ok(meetingService.getAllMeetings());
    }

    @GetMapping("/type/{meetingTypeId}")
    public ResponseEntity<List<Meeting>> getMeetingsByType(@PathVariable Long meetingTypeId) {
        return ResponseEntity.ok(meetingService.getMeetingsByMeetingType(meetingTypeId));
    }

    @PostMapping("/{id}/details")
    public ResponseEntity<Meeting> addMeetingDetails(@PathVariable Long id, @RequestBody MeetingDTO meetingDTO) {
        Meeting updatedMeeting = meetingService.addMeetingDetails(id, meetingDTO);
        return ResponseEntity.ok(updatedMeeting);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeeting(@PathVariable Long id) {
        meetingService.deleteMeeting(id);
        return ResponseEntity.ok().build();
    }
}