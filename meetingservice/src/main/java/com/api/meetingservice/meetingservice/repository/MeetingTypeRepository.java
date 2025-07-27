// MeetingTypeRepository.java
package com.api.meetingservice.meetingservice.repository;

import com.api.meetingservice.meetingservice.models.MeetingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingTypeRepository extends JpaRepository<MeetingType, Long> {
    MeetingType findByName(String name);
}