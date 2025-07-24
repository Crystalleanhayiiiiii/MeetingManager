package com.example.room.entity;
import jakarta.persistence.*;

@Entity
public class Locations {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer locationId;

    @Column(nullable = false)
    private String locationName;

    private String address;

    // Getters and Setters
    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
