package com.agendatask.controller;

import com.agendatask.entity.MeetingMinute;
import com.agendatask.service.MeetingMinuteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/minutes")
public class MeetingMinuteController {
    @Autowired
    private MeetingMinuteService service;

    @GetMapping("/meeting/{meetingId}")
    public Optional<MeetingMinute> getByMeetingId(@PathVariable String meetingId) {
        return service.getByMeetingId(meetingId);
    }

    @PostMapping
    public MeetingMinute create(@RequestBody MeetingMinute minute) {
        return service.create(minute);
    }
    @PutMapping("/{id}")
    public MeetingMinute update(@PathVariable Long id, @RequestBody MeetingMinute minute) {
        return service.update(id, minute);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
