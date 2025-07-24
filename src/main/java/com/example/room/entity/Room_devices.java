package com.example.room.entity;
import jakarta.persistence.*;
@Entity
public class Room_devices {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer roomDeviceId;

	    @ManyToOne
	    @JoinColumn(name = "room_id", referencedColumnName = "roomId")
	    private Rooms room;

	    @ManyToOne
	    @JoinColumn(name = "device_id", referencedColumnName = "deviceId")
	    private Devices device;

	    @Column(nullable = false)
	    private Integer quantity = 1;

	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    private DeviceStatus status = DeviceStatus.AVAILABLE;

	    // Getters and Setters
	    public Integer getRoomDeviceId() {
	        return roomDeviceId;
	    }

	    public void setRoomDeviceId(Integer roomDeviceId) {
	        this.roomDeviceId = roomDeviceId;
	    }

	    public Rooms getRoom() {
	        return room;
	    }

	    public void setRoom(Rooms room) {
	        this.room = room;
	    }

	    public Devices getDevice() {
	        return device;
	    }

	    public void setDevice(Devices device) {
	        this.device = device;
	    }

	    public Integer getQuantity() {
	        return quantity;
	    }

	    public void setQuantity(Integer quantity) {
	        this.quantity = quantity;
	    }

	    public DeviceStatus getStatus() {
	        return status;
	    }

	    public void setStatus(DeviceStatus status) {
	        this.status = status;
	    }
}
