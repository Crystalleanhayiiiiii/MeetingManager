package com.meetingservice.DTO;

import java.util.List;

public class AddParticipantsRequest {
    private List<Long> userIds;

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public AddParticipantsRequest() {
    }

}
