package com.example.room.controller;

import com.example.room.entity.*;
import com.example.room.exception.ResourceNotFoundException;
import com.example.room.service.RoomService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping("/create")
    public ResponseEntity<Rooms> createRoom(@RequestBody Rooms room) {
        try {
            Rooms newRoom = roomService.createRoom(room);
            return new ResponseEntity<>(newRoom, HttpStatus.CREATED); // Trả về 201 khi tạo thành công
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // Trả về lỗi nếu gặp vấn đề
        }
    }
    
    
    // API để lấy danh sách tất cả các phòng
    @GetMapping("/all")
    public ResponseEntity<List<Rooms>> getAllRooms() {
        List<Rooms> rooms = roomService.getAllRooms();
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }
    
	// Lấy phòng theo ID
	@GetMapping("/{roomId}")
	public ResponseEntity<Rooms> getRoom(@PathVariable Integer roomId) {
		try {
			Rooms room = roomService.getRoomById(roomId);
			return new ResponseEntity<>(room, HttpStatus.OK);
		} catch (ResourceNotFoundException ex) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Trả về mã 404 nếu không tìm thấy
		}
	}
	@GetMapping("/location/{locationId}")
	public ResponseEntity<List<Rooms>> getRoomsByLocation(@PathVariable Integer locationId) {
	    List<Rooms> rooms = roomService.getRoomsByLocation(locationId);
	    if (rooms.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // Trả về 204 nếu không có phòng nào ở địa chỉ này
	    }
	    return new ResponseEntity<>(rooms, HttpStatus.OK);  // Trả về danh sách phòng với mã 200 OK
	}
	
	@GetMapping("/location/{locationId}/floors")
	public ResponseEntity<List<Floors>> getFloorsByLocation(@PathVariable Integer locationId) {
	    List<Floors> floors = roomService.getFloorsByLocation(locationId);
	    if (floors.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // Trả về 204 nếu không có tầng nào ở địa chỉ này
	    }
	    return new ResponseEntity<>(floors, HttpStatus.OK);  // Trả về danh sách tầng với mã 200 OK
	}
	@GetMapping("/location/{locationId}/floor/{floorId}/rooms")
	public ResponseEntity<List<Rooms>> getRoomsByFloorAndLocation(@PathVariable Integer locationId, @PathVariable Integer floorId) {
	    List<Rooms> rooms = roomService.getRoomsByFloorAndLocation(locationId, floorId);
	    if (rooms.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // Trả về 204 nếu không có phòng nào ở tầng và địa chỉ này
	    }
	    return new ResponseEntity<>(rooms, HttpStatus.OK);  // Trả về danh sách phòng với mã 200 OK
	}

	
	@GetMapping("/floor/{floorId}")
	public ResponseEntity<List<Rooms>> getRoomsByFloor(@PathVariable Integer floorId) {
	    List<Rooms> rooms = roomService.getRoomsByFloor(floorId);
	    if (rooms.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Trả về 204 nếu không có phòng nào ở tầng này
	    }
	    return new ResponseEntity<>(rooms, HttpStatus.OK); // Trả về danh sách phòng ở tầng yêu cầu
	}
	
	
    @PutMapping("/{roomId}/status")
    public ResponseEntity<Rooms> updateRoomStatus(@PathVariable Integer roomId, @RequestBody String status) {
        Rooms updatedRoom = roomService.updateRoomStatus(roomId, status);
        return new ResponseEntity<>(updatedRoom, HttpStatus.OK);
    }
    
}
