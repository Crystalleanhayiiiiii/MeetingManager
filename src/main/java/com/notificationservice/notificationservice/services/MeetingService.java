package com.notificationservice.notificationservice.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.meetingservice.services.UserDetailResponse;
import com.notificationservice.notificationservice.models.MeetingResponse;
import com.notificationservice.notificationservice.models.Room;
import com.notificationservice.notificationservice.models.Task;
// import com.notificationservice.notificationservice.models.UserDetailResponse;

import jakarta.mail.MessagingException;

@Service
public class MeetingService {

    private final MailService emailService;
    private final RestTemplate restTemplate;

    // Dùng để build deep-link đến portal/app của bạn
    @Value("${app.portal-base-url:https://portal.example.com}")
    private String portalBaseUrl;

    @Autowired
    public MeetingService(MailService emailService, RestTemplate restTemplate) {
        this.emailService = emailService;
        this.restTemplate = restTemplate;
    }

    /* ======================== Public APIs ======================== */

    // Gửi email mời họp cho danh sách userId
    public void sendEmailToParticipants(Long meetingId, List<Long> userIds) throws MessagingException {
        if (userIds == null || userIds.isEmpty())
            return;

        MeetingResponse meeting = getMeetingDetailsById(meetingId);
        Room room = null;
        if (!isOnline(meeting) && meeting.getRoomId() != null) {
            room = getRoomDetailsById(meeting.getRoomId());
        }

        String subject = "[INVITE] " + safe(meeting.getTitle()) + " – " + shortTimeRange(meeting);
        String body = generateMeetingEmailBody(meeting, room);

        for (Long uid : userIds) {
            if (uid == null)
                continue;
            String userEmail = getUserEmailById(uid);
            if (isBlank(userEmail))
                continue;

            try {
                emailService.sendMeetingNotification(userEmail, subject, body, null);
            } catch (Exception ex) {
                // TODO: thay System.err bằng logger của bạn
                System.err.println(
                        "Send mail failed for userId=" + uid + ", email=" + userEmail + ": " + ex.getMessage());
                // không throw để tiếp tục gửi các user khác
            }
        }
    }

    // Giao nhiệm vụ (task) cho 1 user (ví dụ assignee)
    public void sendTaskEmailToAssignee(Long meetingId, Long taskId, Long userId) throws MessagingException {
        MeetingResponse meeting = getMeetingDetailsById(meetingId);
        Task task = getTaskDetailsById(taskId);
        String userEmail = getUserEmailById(userId);

        if (!isBlank(userEmail)) {
            String subject = "[ACTION REQUIRED] " + safe(task.getTitle()) + " – hạn "
                    + formatDateTime(task.getDueDate());
            String body = generateEmailBodyForTask(meeting, task);
            emailService.sendMeetingNotification(userEmail, subject, body, null);
        }
    }

    /* ======================== Data Fetching ======================== */

    private MeetingResponse getMeetingDetailsById(Long meetingId) {
        String url = "http://192.168.1.164:8083/api/meetings/" + meetingId;
        return restTemplate.getForObject(url, MeetingResponse.class);
    }
    // private MeetingResponse getMeetingDetailsById(Long meetingId) {
    // String url = "http://192.168.1.164:8083/api/meetings/" + meetingId;
    // try {
    // // Gọi API và ánh xạ dữ liệu JSON vào đối tượng MeetingResponse
    // MeetingResponse meeting = restTemplate.getForObject(url,
    // MeetingResponse.class);

    // if (meeting == null) {
    // throw new Exception("Không nhận được dữ liệu cuộc họp từ API.");
    // }

    // return meeting;
    // } catch (Exception e) {
    // // Log lỗi và trả về null hoặc thông báo lỗi
    // System.err.println("Error fetching meeting details: " + e.getMessage());
    // return null;
    // }
    // }

    private Task getTaskDetailsById(Long taskId) {
        // SỬA endpoint đúng về /api/tasks/{id}
        String url = "http://192.168.1.164:8083/tasks/" + taskId;
        return restTemplate.getForObject(url, Task.class);
    }

    private Room getRoomDetailsById(Long roomId) {
        String url = "http://192.168.1.164:8083/rooms/" + roomId;
        return restTemplate.getForObject(url, Room.class);
    }

    // private String getUserEmailById(Long userId) {
    // String url = "http://172.16.1.248:8083/users/userDetail/" + userId;
    // UserDetailResponse response = restTemplate.getForObject(url,
    // UserDetailResponse.class);
    // return (response != null) ? response.getEmail() : null;
    // }

    private String getUserEmailById(Long userId) {
        if (userId == null)
            return null;
        String url = "http://192.168.1.164:8083/users/userDetail/" + userId;
        UserDetailResponse response = restTemplate.getForObject(url, UserDetailResponse.class);
        return (response != null) ? response.getEmail() : null;
    }
    /* ======================== Email Templates ======================== */

    // Email mời họp – HTML gọn, chuyên nghiệp, inline CSS
    private String generateMeetingEmailBody(MeetingResponse m, Room r) {
        String meetingLink = composeMeetingDeepLink(m); // tạo link "Xem chi tiết"
        String timeRange = formatTimeRange(m.getStartTime(), m.getEndTime());

        StringBuilder html = new StringBuilder(1600);
        html.append("<table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" ")
                .append("style=\"background:#f5f7fb;padding:24px 0;font-family:Arial,Helvetica,sans-serif;\">")
                .append("<tr><td align=\"center\">")
                .append("<table role=\"presentation\" width=\"600\" cellspacing=\"0\" cellpadding=\"0\" style=\"background:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 4px 16px rgba(0,0,0,0.08)\">")

                // Header
                .append("<tr><td style=\"background:#0f172a;color:#ffffff;padding:20px 24px;font-size:18px;font-weight:bold\">")
                .append("Thư mời cuộc họp")
                .append("</td></tr>")

                // Body
                .append("<tr><td style=\"padding:24px;color:#0f172a;line-height:1.6;font-size:14px\">")
                .append("<p>Chào Anh/Chị,</p>")
                .append("<p>Trân trọng mời Anh/Chị tham dự cuộc họp <strong>")
                .append(escape(safe(m.getTitle())))
                .append("</strong>.</p>")

                // Details card
                .append("<table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" ")
                .append("style=\"background:#f8fafc;border:1px solid #e2e8f0;border-radius:8px;margin:16px 0\">")

                .append(rowKV("Thời gian", escape(timeRange)))
                .append(rowKV("Hình thức/Địa điểm", buildLocationSection(m, r)))
                .append(!isBlank(safe(m.getDescription())) ? rowKV("Mô tả", escape(safe(m.getDescription()))) : "")

                .append("</table>");

        // Nếu có lý do hủy (ví dụ mail thông báo hủy)
        if (!isBlank(safe(m.getCancelReason()))) {
            html.append("<p style=\"color:#b91c1c\"><strong>Lý do hủy:</strong> ")
                    .append(escape(safe(m.getCancelReason())))
                    .append("</p>");
        }

        // CTA
        if (!isBlank(meetingLink)) {
            html.append("<div style=\"margin-top:20px\">")
                    .append("<a href=\"").append(escape(meetingLink)).append("\" target=\"_blank\" ")
                    .append("style=\"display:inline-block;padding:10px 16px;background:#2563eb;color:#ffffff;text-decoration:none;border-radius:8px;font-weight:600\">")
                    .append("Xem chi tiết cuộc họp</a>")
                    .append("</div>");
        }

        html.append("<p style=\"margin-top:24px\">Trân trọng,<br/>Ban tổ chức</p>")
                .append("</td></tr>")
                // Footer
                .append("<tr><td style=\"background:#0f172a;color:#94a3b8;padding:12px 24px;font-size:12px\">")
                .append("Email tự động – vui lòng không trả lời trực tiếp.")
                .append("</td></tr>")
                .append("</table></td></tr></table>");

        return html.toString();
    }

    // Email giao nhiệm vụ gắn với cuộc họp
    private String generateEmailBodyForTask(MeetingResponse m, Task t) {
        String meetingLink = composeMeetingDeepLink(m);
        String taskLink = composeTaskDeepLink(t);
        String dueText = formatDateTime(t.getDueDate());

        StringBuilder html = new StringBuilder(1600);
        html.append("<table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" ")
                .append("style=\"background:#f5f7fb;padding:24px 0;font-family:Arial,Helvetica,sans-serif;\">")
                .append("<tr><td align=\"center\">")
                .append("<table role=\"presentation\" width=\"600\" cellspacing=\"0\" cellpadding=\"0\" style=\"background:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 4px 16px rgba(0,0,0,0.08)\">")

                .append("<tr><td style=\"background:#111827;color:#ffffff;padding:20px 24px;font-size:18px;font-weight:bold\">")
                .append("Giao nhiệm vụ")
                .append("</td></tr>")

                .append("<tr><td style=\"padding:24px;color:#0f172a;line-height:1.6;font-size:14px\">")
                .append("<p>Chào Anh/Chị,</p>")
                .append("<p>Bạn được giao nhiệm vụ <strong>").append(escape(safe(t.getTitle())))
                .append("</strong>.</p>")

                .append("<table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" ")
                .append("style=\"background:#f8fafc;border:1px solid #e2e8f0;border-radius:8px;margin:16px 0\">")
                .append(rowKV("Thuộc cuộc họp", escape(safe(m.getTitle()))))
                .append(rowKV("Hạn chót", escape(dueText)))
                .append(!isBlank(safe(t.getDescription())) ? rowKV("Chi tiết", escape(safe(t.getDescription()))) : "")
                .append("</table>");

        // CTA
        html.append("<div style=\"margin-top:20px\">");
        if (!isBlank(taskLink)) {
            html.append("<a href=\"").append(escape(taskLink)).append("\" target=\"_blank\" ")
                    .append("style=\"display:inline-block;padding:10px 16px;background:#2563eb;color:#ffffff;text-decoration:none;border-radius:8px;font-weight:600;margin-right:8px\">")
                    .append("Xem nhiệm vụ</a>");
        }
        if (!isBlank(meetingLink)) {
            html.append("<a href=\"").append(escape(meetingLink)).append("\" target=\"_blank\" ")
                    .append("style=\"display:inline-block;padding:10px 16px;background:#334155;color:#ffffff;text-decoration:none;border-radius:8px;font-weight:600\">")
                    .append("Chi tiết cuộc họp</a>");
        }
        html.append("</div>");

        html.append("<p style=\"margin-top:24px\">Trân trọng,<br/>Ban tổ chức</p>")
                .append("</td></tr>")
                .append("<tr><td style=\"background:#111827;color:#9ca3af;padding:12px 24px;font-size:12px\">")
                .append("Email tự động – vui lòng không trả lời trực tiếp.")
                .append("</td></tr>")
                .append("</table></td></tr></table>");

        return html.toString();
    }

    /* ======================== View Helpers ======================== */

    // Dòng key–value trong card
    private String rowKV(String key, String valueHtml) {
        if (isBlank(valueHtml))
            return "";
        StringBuilder sb = new StringBuilder(256);
        sb.append("<tr>")
                .append("<td style=\"width:160px;padding:10px 12px;color:#334155;font-weight:600;border-bottom:1px solid #e2e8f0\">")
                .append(escape(key))
                .append("</td>")
                .append("<td style=\"padding:10px 12px;color:#0f172a;border-bottom:1px solid #e2e8f0\">")
                .append(valueHtml) // đã escape/compose từ trước
                .append("</td>")
                .append("</tr>");
        return sb.toString();
    }

    // Tạo đoạn HTML “Hình thức/Địa điểm”
    private String buildLocationSection(MeetingResponse m, Room r) {
        boolean online = isOnline(m);

        if (online) {
            String platform = defaultIfBlank(m.getOnlinePlatform(), "Online");
            String link = safe(m.getOnlineLink());
            if (!isBlank(link)) {
                return "<strong>" + escape(platform) + "</strong> — "
                        + "<a href=\"" + escape(link) + "\" target=\"_blank\" rel=\"noopener noreferrer\">"
                        + escape(link) + "</a>";
            } else {
                return "<strong>" + escape(platform) + "</strong> — (Sẽ cập nhật link)";
            }
        }

        // Offline
        StringBuilder sb = new StringBuilder(256);
        if (r != null) {
            // Đổi các getter này cho đúng model Room của bạn (ví dụ: getName(), getLevel(),
            // getAddress()…)
            String roomName = safe(r.getRoomName());
            String floor = safe(r.getFloor());
            String capacity = safe(r.getCapacity()); // nếu capacity là số, đổi sang String.valueOf
            // String address = safe(r.getAddress()); // nếu Room có trường địa chỉ

            boolean printed = false;

            if (!isBlank(roomName)) {
                sb.append(escape(roomName));
                printed = true;
            }
            if (!isBlank(floor)) {
                if (printed)
                    sb.append(" — ");
                sb.append("Tầng ").append(escape(floor));
                printed = true;
            }
            if (!isBlank(capacity)) {
                if (printed)
                    sb.append(", ");
                sb.append("Sức chứa: ").append(escape(capacity));
                printed = true;
            }
            // if (!isBlank(address)) {
            // if (printed)
            // sb.append(", ");
            // sb.append(escape(address));
            // printed = true;
            // }

            if (!printed) {
                sb.append("[Chưa cập nhật phòng họp]");
            }
        } else {
            sb.append("[Chưa cập nhật phòng họp]");
        }
        return sb.toString();
    }

    private boolean isOnline(MeetingResponse m) {
        return !isBlank(m.getOnlineLink()) || !isBlank(m.getOnlinePlatform());
    }

    // Link deep-link đến portal của bạn (chỉnh theo route thực tế)
    private String composeMeetingDeepLink(MeetingResponse m) {
        if (m == null || m.getId() == null)
            return null; // đảm bảo MeetingResponse có getId()
        return portalBaseUrl.replaceAll("/+$", "") + "/meetings/" + m.getId();
    }

    private String composeTaskDeepLink(Task t) {
        if (t == null || t.getId() == null)
            return null; // đảm bảo Task có getId()
        return portalBaseUrl.replaceAll("/+$", "") + "/tasks/" + t.getId();
    }

    // Hiển thị "dd/MM/yyyy HH:mm – dd/MM/yyyy HH:mm"
    private String formatTimeRange(Object startObj, Object endObj) {
        String start = formatDateTime(startObj);
        String end = formatDateTime(endObj);
        if (!isBlank(start) && !isBlank(end))
            return start + " – " + end + " (GMT+7)";
        if (!isBlank(start))
            return start + " (GMT+7)";
        return "";
    }

    private String shortTimeRange(MeetingResponse m) {
        String start = formatDateTime(m.getStartTime());
        return isBlank(start) ? "" : (start + " (GMT+7)");
    }

    // Hỗ trợ LocalDateTime hoặc String
    private String formatDateTime(Object dt) {
        if (dt == null)
            return "";
        if (dt instanceof LocalDateTime) {
            LocalDateTime ldt = (LocalDateTime) dt;
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                    .withZone(ZoneId.of("Asia/Ho_Chi_Minh"));
            return ldt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }
        // Nếu backend trả chuỗi ISO/định dạng sẵn thì trả nguyên
        return String.valueOf(dt);
    }

    /* ======================== Utils ======================== */

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String defaultIfBlank(String s, String def) {
        return isBlank(s) ? def : s;
    }

    // loại script rất cơ bản
    private static String safe(Object o) {
        return (o == null) ? "" : String.valueOf(o).replaceAll("(?i)<script.*?>.*?</script>", "");
    }

    private static String escape(String s) {
        if (s == null)
            return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    // GỬI THỬ VỚI DỮ LIỆU CỨNG (không gọi service khác)
    // public void sendEmailToParticipantsDummy(List<String> emails, boolean online)
    // throws MessagingException {
    // // --- Mock meeting ---
    // MeetingResponse m = new MeetingResponse();
    // m.setId(74L);
    // m.setTitle(online ? "Demo họp ONLINE" : "Demo họp OFFLINE");
    // m.setDescription("Email thử nghiệm từ notification-service (dữ liệu cứng).");
    // m.setStartTime("20/08/2025 09:00");
    // m.setEndTime("20/08/2025 10:00");

    // Room r = null;
    // if (online) {
    // m.setOnlinePlatform("Google Meet");
    // m.setOnlineLink("https://meet.google.com/abc-defg-hij");
    // m.setRoomId(null);
    // } else {
    // m.setOnlinePlatform(null);
    // m.setOnlineLink(null);
    // m.setRoomId(1L); // chỉ để hiển thị minh họa

    // r = new Room();
    // // Đổi các setter này theo model Room thực tế của bạn
    // r.setRoomName("Phòng A1");
    // r.setFloor(5);
    // r.setCapacity(2);
    // // r.setAddress("1200 Bay Street, Toronto, ON");
    // }

    // String subject = "[INVITE] " + safe(m.getTitle()) + " – " +
    // shortTimeRange(m);
    // String body = generateMeetingEmailBody(m, r);

    // for (String email : emails) {
    // if (!isBlank(email)) {
    // emailService.sendMeetingNotification(email.trim(), subject, body, null);
    // }
    // }
    // }

}
