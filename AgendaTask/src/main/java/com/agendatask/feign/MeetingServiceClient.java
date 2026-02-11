package com.agendatask.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "meeting-service")
public interface MeetingServiceClient {
    @GetMapping("/meetings/{meetingId}/participants")
    List<Long> getMeetingParticipants(@PathVariable String meetingId);
}