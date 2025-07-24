package com.example.room.repositoty;

import com.example.room.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; 

@Repository
public interface DeviceRepository extends JpaRepository<Devices, Integer> {
    // JpaRepository tự động cung cấp các phương thức CRUD cơ bản
}
