package com.notificationservice.notificationservice.controller;

import com.notificationservice.notificationservice.models.Meeting;
import com.notificationservice.notificationservice.models.MeetingResponse;
import com.notificationservice.notificationservice.services.MailService;
// import com.notificationservice.notificationservice.services.MailService;
import com.notificationservice.notificationservice.services.MeetingService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

// import javax.mai.*;

@RestController
@RequestMapping("/api/test")
@Validated
public class TestEmailController {

    private final MeetingService meetingService;

    @Autowired
    public TestEmailController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping("/{meetingId}/send-email")
    public String sendEmail(@PathVariable Long meetingId, @RequestBody List<Long> userIds) {
        try {
            // Send email to participants
            meetingService.sendEmailToParticipants(meetingId, userIds);
            return "Email has been sent successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send email: " + e.getMessage();
        }
    }

    /// test
    // @GetMapping("/ping")
    // public Map<String, String> ping() {
    // return Map.of("status", "ok");
    // }

    // @PostMapping("/meetings/invite")
    // public ResponseEntity<?> inviteParticipants(@RequestBody @Valid
    // MeetingResponse req) {
    // try {
    // meetingService.sendEmailToParticipants(req.getId(), req.getParticipants());
    // return ResponseEntity.accepted().body(Map.of(
    // "success", true,
    // "message", "Đã trigger gửi mail mời họp",
    // "meetingId", String.valueOf(req.getMeetingId()),
    // "userCount", String.valueOf(req.getUserIds().size())));
    // } catch (MessagingException e) {
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
    // "success", false,
    // "error", "MessagingException: " + e.getMessage()));
    // } catch (Exception e) {
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
    // "success", false,
    // "error", e.getClass().getSimpleName() + ": " + e.getMessage()));
    // }
    // }

    @GetMapping("/invite")
    public String invite(@RequestParam long meetingId,
            @RequestParam(required = false) String userIds,
            @RequestParam(required = false) List<Long> userIdsList) throws MessagingException {

        List<Long> ids = parseUserIds(userIds, userIdsList);
        if (ids.isEmpty()) {
            return "ERR: userIds empty";
        }

        meetingService.sendEmailToParticipants(meetingId, ids);
        return "OK: sent invites for meeting " + meetingId + " to " + ids.size() + " users -> " + ids;
    }

    private List<Long> parseUserIds(String userIdsStr, List<Long> userIdsList) {
        // Ưu tiên list nếu client gọi ?userIds=1&userIds=2...
        if (userIdsList != null && !userIdsList.isEmpty()) {
            return userIdsList.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        }
        if (userIdsStr == null || userIdsStr.trim().isEmpty())
            return List.of();

        // Hỗ trợ dạng "1,2,3" hoặc "[1,2,3]"
        String cleaned = userIdsStr.trim();
        if (cleaned.startsWith("["))
            cleaned = cleaned.substring(1);
        if (cleaned.endsWith("]"))
            cleaned = cleaned.substring(0, cleaned.length() - 1);

        String[] parts = cleaned.split(",");
        List<Long> out = new ArrayList<>();
        for (String p : parts) {
            String s = p.trim();
            if (s.isEmpty())
                continue;
            try {
                out.add(Long.parseLong(s));
            } catch (NumberFormatException ignore) {
                /* bỏ qua phần tử lỗi */ }
        }
        return out.stream().distinct().collect(Collectors.toList());
    }

    @GetMapping("/invite/dummy")
    public String inviteDummy(@RequestParam String emails,
            @RequestParam(defaultValue = "true") boolean online) throws MessagingException {
        var list = Arrays.stream(emails.split(","))
                .map(String::trim).filter(s -> !s.isEmpty())
                .distinct().collect(Collectors.toList());

        meetingService.sendEmailToParticipantsDummy(list, online);
        return "OK: sent dummy invites (" + (online ? "ONLINE" : "OFFLINE") + ") to " + list;
    }
}