package com.example.room.entity;
import jakarta.persistence.*;
@Entity
public class Devices {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer deviceId;

	    @Column(nullable = false)
	    private String deviceName;

	    @Column(nullable = false)
	    private String deviceType;

	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    private DeviceStatus status = DeviceStatus.AVAILABLE;

	    // Getters and Setters
	    public Integer getDeviceId() {
	        return deviceId;
	    }

	    public void setDeviceId(Integer deviceId) {
	        this.deviceId = deviceId;
	    }

	    public String getDeviceName() {
	        return deviceName;
	    }

	    public void setDeviceName(String deviceName) {
	        this.deviceName = deviceName;
	    }

	    public String getDeviceType() {
	        return deviceType;
	    }

	    public void setDeviceType(String deviceType) {
	        this.deviceType = deviceType;
	    }

	    public DeviceStatus getStatus() {
	        return status;
	    }

	    public void setStatus(DeviceStatus status) {
	        this.status = status;
	    }
}
