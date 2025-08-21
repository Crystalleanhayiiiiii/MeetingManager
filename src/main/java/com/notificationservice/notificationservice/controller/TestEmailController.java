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
@RequestMapping("/api/notifications")
@Validated
public class TestEmailController {

    private final MeetingService meetingService;

    @Autowired
    public TestEmailController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    /**
     * API gửi email mời tham gia cuộc họp cho danh sách userId
     * 
     * @param meetingId: ID của cuộc họp
     * @param userIds:   Danh sách userId nhận email mời
     * @return ResponseEntity với thông báo gửi thành công hay thất bại
     */
    @PostMapping("/{meetingId}/send-email")
    public ResponseEntity<String> sendEmail(@PathVariable Long meetingId, @RequestBody List<Long> userIds) {
        try {
            // Gửi email mời tham gia cuộc họp cho danh sách userId
            meetingService.sendEmailToParticipants(meetingId, userIds);
            return ResponseEntity.ok("Email đã được gửi thành công!");
        } catch (MessagingException e) {
            // Xử lý khi có lỗi gửi email
            return ResponseEntity.status(500).body("Gửi email thất bại: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    /**
     * API gửi email thông báo nhiệm vụ cho người được giao nhiệm vụ
     *
     * @param meetingId: ID của cuộc họp
     * @param taskId:    ID của nhiệm vụ
     * @param userId:    ID của người được giao nhiệm vụ
     * @return ResponseEntity với thông báo gửi thành công hay thất bại
     */
    @PostMapping("/{meetingId}/task/{taskId}/send-email/{userId}")
    public ResponseEntity<String> sendTaskEmail(@PathVariable Long meetingId,
            @PathVariable Long taskId,
            @PathVariable Long userId) {
        try {
            // Gửi email thông báo nhiệm vụ cho người nhận
            meetingService.sendTaskEmailToAssignee(meetingId, taskId, userId);
            return ResponseEntity.ok("Email thông báo nhiệm vụ đã được gửi thành công!");
        } catch (MessagingException e) {
            // Xử lý khi có lỗi gửi email
            return ResponseEntity.status(500).body("Gửi email thất bại: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Đã xảy ra lỗi: " + e.getMessage());
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

    // @GetMapping("/invite/dummy")
    // public String inviteDummy(@RequestParam String emails,
    // @RequestParam(defaultValue = "true") boolean online) throws
    // MessagingException {
    // var list = Arrays.stream(emails.split(","))
    // .map(String::trim).filter(s -> !s.isEmpty())
    // .distinct().collect(Collectors.toList());

    // meetingService.sendEmailToParticipantsDummy(list, online);
    // return "OK: sent dummy invites (" + (online ? "ONLINE" : "OFFLINE") + ") to "
    // + list;
    // }
}