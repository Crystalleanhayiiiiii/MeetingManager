package com.agendatask.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "agendas")
public class Agenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String meetingId;

    @Column(nullable = false)
    private String title;

    private String description;
    private Integer duration;
    private Integer orderIndex;
	public Agenda() {
		
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
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
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Integer getOrderIndex() {
		return orderIndex;
	}
	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

    // Getters and Setters
}
