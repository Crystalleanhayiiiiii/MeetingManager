// OnlineMeetingDetailRepository.java
package com.api.meetingservice.meetingservice.repository;

import com.api.meetingservice.meetingservice.models.OnlineMeetingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlineMeetingDetailRepository extends JpaRepository<OnlineMeetingDetail, Long> {
}
