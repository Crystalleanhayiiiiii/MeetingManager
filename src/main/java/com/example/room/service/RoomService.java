package com.example.room.service;

import com.example.room.entity.*;
import com.example.room.repositoty.FloorRepository;
import com.example.room.repositoty.RoomRepository;
import com.example.room.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
	@Autowired
    private RoomRepository roomRepository;
	@Autowired
	private FloorRepository floorRepository;
	
    public Rooms createRoom(Rooms room) {
        return roomRepository.save(room);
    }
    
    public List<Rooms> getAllRooms() {
        return roomRepository.findAll();
    }
    
    // Phương thức lấy phòng theo ID
    public Rooms getRoomById(Integer roomId) {
        Optional<Rooms> room = roomRepository.findByRoomId(roomId);
        if (room.isEmpty()) {
            // Thêm thông báo lỗi chi tiết
            throw new ResourceNotFoundException("Room with ID " + roomId + " not found");
        }
        return room.get();
    }
    
    public List<Rooms> getRoomsByLocation(Integer locationId) {
        return roomRepository.findByFloorLocationLocationId(locationId);  // Tìm các phòng thuộc địa chỉ (location)
    }
    
    public List<Floors> getFloorsByLocation(Integer locationId) {
        return floorRepository.findByLocationLocationId(locationId);  // Tìm các tầng thuộc địa chỉ (location)
    }


    public List<Rooms> getRoomsByFloor(Integer floorId) {
        return roomRepository.findByFloorFloorId(floorId); 
    }
    
    public List<Rooms> getRoomsByFloorAndLocation(Integer locationId, Integer floorId) {
        return roomRepository.findByFloorLocationLocationIdAndFloorFloorId(locationId, floorId);  // Tìm các phòng theo địa chỉ và tầng
    }

    
    public Rooms updateRoomStatus(Integer roomId, String status) {
        Rooms rooms = getRoomById(roomId);
        rooms.setStatus(RoomStatus.valueOf(status));
        return roomRepository.save(rooms);
    }
}
