package com.example.room.entity;

import jakarta.persistence.*;

@Entity
public class Floors {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer floorId;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "locationId")
    private Locations location;

    @Column(nullable = false)
    private Integer floorNumber;

    // Getters and Setters
    public Integer getFloorId() {
        return floorId;
    }

    public void setFloorId(Integer floorId) {
        this.floorId = floorId;
    }

    public Locations getLocation() {
        return location;
    }

    public void setLocation(Locations location) {
        this.location = location;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }
}
