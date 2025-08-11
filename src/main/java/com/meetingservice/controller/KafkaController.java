package com.meetingservice.controller;

import com.meetingservice.services.KafkaProducerService;
import com.meetingservice.services.MeetingProducerService;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    private final KafkaProducerService kafkaProducerService;

    // private final MeetingProducerService meetingProducerService;

    public KafkaController(KafkaProducerService kafkaProducerService, MeetingProducerService meetingProducerService) {
        this.kafkaProducerService = kafkaProducerService;
        // this.meetingProducerService = meetingProducerService;
    }

    @GetMapping("/send")
    public String sendMessage(@RequestParam String message) {
        kafkaProducerService.sendMessage(message);
        return "Message sent successfully";
    }

    // @GetMapping("/send1")
    // public String sendMeeting(Meeting meeting) {
    // meetingProducerService.sendMeeting(meeting);
    // return "Meeting sent successfully" + meeting.getTitle();
    // }

}
