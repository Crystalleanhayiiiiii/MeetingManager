package com.meetingservice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.meetingservice.DTO.AddParticipantsRequest;
import com.meetingservice.DTO.CancelMeetingRequest;
import com.meetingservice.DTO.CreateMeetingRequest;
import com.meetingservice.DTO.UpdateMeetingRequest;
import com.meetingservice.models.Meeting;

import com.meetingservice.services.*;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController {

    // @Autowired
    // private MeetingRepository meetingRepository;

    // private final KafkaProducerService kafkaProducerService = null;

    // @GetMapping("/send1")
    // public String sendMeetingsToKafka() {
    // Iterable<Meeting> meetings = meetingRepository.findAll();
    // meetings.forEach(meeting -> {
    // String message = "Meeting: " + meeting.getTitle() + " at " +
    // meeting.getStartTime();
    // kafkaProducerService.sendMessage(message);
    // });
    // return "Meetings sent to Kafka!";
    // }

    // private final MeetingService meetingService;
    // @Autowired
    // private final MeetingProducerService meetingProducerService;

    // public MeetingController(MeetingService meetingService,
    // MeetingProducerService meetingProducerService) {
    // this.meetingService = meetingService;
    // this.meetingProducerService = meetingProducerService;
    // }

    // Endpoint to retrieve meeting by ID and send to Kafka
    // @GetMapping("/sendMeetingById")
    // public String sendMeetingById(@RequestParam Long id) {
    // // Retrieve the meeting from the database
    // Meeting meeting = meetingService.getMeetingById(id);
    // if (meeting == null) {
    // return "Meeting not found with id: " + id;
    // }

    // // Send the meeting to Kafka
    // meetingProducerService.sendMeeting("success", meeting);
    // return "Meeting sent to Kafka successfully: " + meeting.getTitle();
    // }

    // // Đẩy meeting lên Kafka để test Consumer
    // @PostMapping("/sendtest")
    // public String sendMeeting(@RequestBody Meeting meeting) {
    // meetingProducerService.sendMeeting(null, meeting);
    // return "Meeting sent to Kafka: " + meeting.getTitle();
    // }

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping
    public ResponseEntity<Meeting> create(@RequestBody CreateMeetingRequest req) {
        return ResponseEntity.ok(meetingService.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Meeting> update(@PathVariable Long id,
            @RequestParam Long organizerId,
            @RequestBody UpdateMeetingRequest req) {
        return ResponseEntity.ok(meetingService.update(id, organizerId, req));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id,
            @RequestParam Long organizerId,
            @RequestBody(required = false) CancelMeetingRequest req) {
        meetingService.cancel(id, organizerId, req == null ? null : req.getReason());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/participants")
    public ResponseEntity<Map<String, Object>> addParticipants(@PathVariable Long id,
            @RequestParam Long organizerId,
            @RequestBody AddParticipantsRequest req) {
        List<Long> added = meetingService.addParticipants(id, organizerId, req.getUserIds());
        Map<String, Object> body = new HashMap<>();
        body.put("added", added);
        body.put("count", added.size());
        return ResponseEntity.ok(body);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long id,
            @RequestParam Long adminId) {
        meetingService.approve(id, adminId);
        return ResponseEntity.ok().build();
    }
    // ===== NEW: Query meetings theo vai trò/trạng thái =====

    // Upcoming: cả organizer + attendee
    @GetMapping("/user/{userId}/upcoming")
    public ResponseEntity<Page<Meeting>> upcomingAll(@PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(meetingService.getUpcomingAll(userId, page, size));
    }

    // Upcoming: chỉ organizer
    @GetMapping("/user/{userId}/upcoming/organizer")
    public ResponseEntity<Page<Meeting>> upcomingOrganizer(@PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(meetingService.getUpcomingOrganizer(userId, page, size));
    }

    // Upcoming: chỉ attendee (khác organizer)
    @GetMapping("/user/{userId}/upcoming/attendee")
    public ResponseEntity<Page<Meeting>> upcomingAttendee(@PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(meetingService.getUpcomingAttendee(userId, page, size));
    }

    // Completed (đã kết thúc): cả organizer + attendee
    @GetMapping("/user/{userId}/completed")
    public ResponseEntity<Page<Meeting>> completedAll(@PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(meetingService.getCompletedAll(userId, page, size));
    }

    @GetMapping("/user/{userId}/all")
    public ResponseEntity<List<Meeting>> all(@PathVariable Long userId) {
        return ResponseEntity.ok(meetingService.getAllByUser(userId));
    }
}
