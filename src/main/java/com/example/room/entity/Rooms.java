package com.example.room.entity;
//import javax.persistence.*;

import jakarta.persistence.*;
@Entity
public class Rooms {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roomId;

    @Column(nullable = false)
    private String roomName;

    @Column(nullable = false)
    private Integer capacity;

    @ManyToOne
    @JoinColumn(name = "floor_id", referencedColumnName = "floorId")
    private Floors floor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status = RoomStatus.AVAILABLE;

    // Getters and Setters
    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Floors getFloor() {
        return floor;
    }

    public void setFloor(Floors floor) {
        this.floor = floor;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }
}
