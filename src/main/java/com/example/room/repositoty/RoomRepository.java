package com.example.room.repositoty;
import com.example.room.entity.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface RoomRepository extends JpaRepository<Rooms, Integer> {
	Optional<Rooms> findByRoomId(Integer roomId);
	// Tìm các phòng theo trạng thái
    List<Rooms> findByStatus(RoomStatus status);
    
    List<Rooms> findByFloorLocationLocationId(Integer locationId);
    
    List<Rooms> findByFloorLocationLocationIdAndFloorFloorId(Integer locationId, Integer floorId);
    // Tìm các phòng theo Floor ID
    List<Rooms> findByFloorFloorId(Integer floorId);
    
    // Tìm phòng theo Room Name
    Rooms findByRoomName(String roomName);
}
