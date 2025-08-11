package com.notificationservice.notificationservice.models;

public class Meeting {
    private Long id;

    private String title;

    private String description;

    // private String startTime;
    // private String endTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Meeting() {/// need
        // Default constructor cần thiết cho deserialization
    }
}
