package com.api.meetingservice.meetingservice.DTO;



import lombok.Data;

import java.time.LocalDateTime;
@Data
public class CreateMeetingRequest {
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long meetingTypeId;
    private String status;
    private OnlineMeetingDetailDto onlineDetails;
    private OfflineMeetingDetailDto offlineDetails;

    @Data
    public static class OnlineMeetingDetailDto {
        private String onlineLink;
        private String platform;
    }

    @Data
    public static class OfflineMeetingDetailDto {
        private String roomLocation;
        private String equipmentNeeded;
        private Integer quantity;
    }
}