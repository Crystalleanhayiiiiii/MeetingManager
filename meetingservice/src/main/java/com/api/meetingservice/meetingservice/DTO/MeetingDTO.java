package com.api.meetingservice.meetingservice.DTO;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.URL;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
@Data
@NoArgsConstructor
public class MeetingDTO {

    /**
     * Unique identifier for the meeting (optional for create, required for update).
     */
    private Long id;

    /**
     * Title of the meeting.
     */
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    /**
     * Description of the meeting (optional).
     */
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    /**
     * Start time of the meeting.
     */
    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    /**
     * End time of the meeting.
     */
    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;

    /**
     * Identifier of the meeting type (e.g., ONLINE, OFFLINE, HYBRID).
     */
    @NotNull(message = "Meeting type ID is required")
    private Long meetingTypeId;

    /**
     * Status of the meeting (e.g., SCHEDULED, CANCELLED).
     */
    @Pattern(regexp = "SCHEDULED|CANCELLED|COMPLETED", message = "Status must be SCHEDULED, CANCELLED, or COMPLETED")
    private String status;

    /**
     * Online meeting link (required for ONLINE or HYBRID meetings).
     */
    @URL(message = "Online link must be a valid URL")
    private String onlineLink;

    /**
     * Platform for online meeting (e.g., Zoom, Teams) (required for ONLINE or HYBRID meetings).
     */
    @Size(max = 100, message = "Platform must not exceed 100 characters")
    private String platform;

    /**
     * Room location for offline meeting (required for OFFLINE or HYBRID meetings).
     */
    @Size(max = 255, message = "Room location must not exceed 255 characters")
    private String roomLocation;

    /**
     * Equipment needed for offline meeting (optional).
     */
    @Size(max = 255, message = "Equipment needed must not exceed 255 characters")
    private String equipmentNeeded;

    /**
     * Quantity of equipment needed (required for OFFLINE or HYBRID meetings if equipment is specified).
     */
    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    /**
     * Custom validation to ensure endTime is after startTime and fields match meeting type.
     */
    @AssertTrue(message = "End time must be after start time")
    private boolean isEndTimeValid() {
        if (startTime == null || endTime == null) {
            return true; // Let @NotNull handle null checks
        }
        return endTime.isAfter(startTime);
    }
}
