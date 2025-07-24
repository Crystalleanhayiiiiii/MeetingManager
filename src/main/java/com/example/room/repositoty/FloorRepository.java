package com.example.room.repositoty;

import com.example.room.entity.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FloorRepository extends JpaRepository<Floors, Integer> {
    // JpaRepository tự động cung cấp các phương thức CRUD cơ bản
	List<Floors> findByLocationLocationId(Integer locationId);
}
