package com.agendatask.service;

import com.agendatask.entity.MeetingMinute;
import com.agendatask.repository.MeetingMinuteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MeetingMinuteService {
    @Autowired
    private MeetingMinuteRepository repo;

    public Optional<MeetingMinute> getByMeetingId(String meetingId) {
        return repo.findByMeetingId(meetingId);
    }

    public MeetingMinute create(MeetingMinute minute) {
        return repo.save(minute);
    }
    public MeetingMinute update(Long id, MeetingMinute minute) {
        return repo.findById(id)
            .map(existing -> {
                // Cập nhật các field cho phép chỉnh sửa
                existing.setContent(minute.getContent());
                existing.setMeetingId(minute.getMeetingId());
                existing.setCreatedBy(minute.getCreatedBy());
                // Không đổi createdAt trừ khi bạn muốn cho phép cập nhật
                return repo.save(existing);
            })
            .orElseThrow(() -> new RuntimeException("MeetingMinute not found with id: " + id));
    }


    public void delete(Long id) {
        repo.deleteById(id);
    }
}
