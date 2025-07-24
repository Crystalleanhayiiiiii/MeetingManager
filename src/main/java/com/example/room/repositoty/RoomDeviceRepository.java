package com.example.room.repositoty;

import com.example.room.entity.Room_devices;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomDeviceRepository extends JpaRepository<Room_devices, Integer> {
    // Tìm thiết bị trong phòng theo roomId
    List<Room_devices> findByRoomRoomId(Integer roomId);
}