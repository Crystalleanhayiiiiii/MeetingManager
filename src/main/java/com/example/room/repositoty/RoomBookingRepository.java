package com.example.room.repositoty;

import com.example.room.entity.Room_bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomBookingRepository extends JpaRepository<Room_bookings, Integer> {
    // Tìm các đặt phòng theo phòng
    List<Room_bookings> findByRoomRoomId(Integer roomId);

    // Tìm các đặt phòng theo trạng thái
    List<Room_bookings> findByStatus(String status);
}