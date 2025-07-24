package com.example.room.repositoty;

import com.example.room.entity.Locations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Locations, Integer> {
    // JpaRepository tự động cung cấp các phương thức CRUD cơ bản
}
