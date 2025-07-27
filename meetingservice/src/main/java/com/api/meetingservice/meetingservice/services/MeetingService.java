package com.api.meetingservice.meetingservice.services;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.meetingservice.meetingservice.DTO.MeetingDTO;

import com.api.meetingservice.meetingservice.models.Meeting;
import com.api.meetingservice.meetingservice.models.MeetingType;
import com.api.meetingservice.meetingservice.models.OfflineMeetingDetail;
import com.api.meetingservice.meetingservice.models.OnlineMeetingDetail;
import com.api.meetingservice.meetingservice.repository.MeetingRepository;
import com.api.meetingservice.meetingservice.repository.MeetingTypeRepository;



@Service
public class MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private MeetingTypeRepository meetingTypeRepository;

    @Transactional
    public Meeting createMeeting(MeetingDTO meetingDTO) {
        MeetingType meetingType = meetingTypeRepository.findById(meetingDTO.getMeetingTypeId())
                .orElseThrow(() -> new RuntimeException("Meeting type not found"));

        Meeting meeting = new Meeting();
        meeting.setTitle(meetingDTO.getTitle());
        meeting.setDescription(meetingDTO.getDescription());
        meeting.setStartTime(meetingDTO.getStartTime());
        meeting.setEndTime(meetingDTO.getEndTime());
        meeting.setMeetingType(meetingType);
        meeting.setStatus(meetingDTO.getStatus());

        if ("ONLINE".equals(meetingType.getName())) {
            OnlineMeetingDetail onlineDetail = new OnlineMeetingDetail();
            onlineDetail.setMeeting(meeting);
            onlineDetail.setOnlineLink(meetingDTO.getOnlineLink());
            onlineDetail.setPlatform(meetingDTO.getPlatform());
            meeting.setOnlineMeetingDetail(onlineDetail);
        } else if ("OFFLINE".equals(meetingType.getName())) {
            OfflineMeetingDetail offlineDetail = new OfflineMeetingDetail();
            offlineDetail.setMeeting(meeting);
            offlineDetail.setRoomLocation(meetingDTO.getRoomLocation());
            offlineDetail.setEquipmentNeeded(meetingDTO.getEquipmentNeeded());
            offlineDetail.setQuantity(meetingDTO.getQuantity());
            meeting.setOfflineMeetingDetail(offlineDetail);
        }

        return meetingRepository.save(meeting);
    }

    @Transactional
    public Meeting updateMeeting(Long id, MeetingDTO meetingDTO) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        MeetingType meetingType = meetingTypeRepository.findById(meetingDTO.getMeetingTypeId())
                .orElseThrow(() -> new RuntimeException("Meeting type not found"));

        meeting.setTitle(meetingDTO.getTitle());
        meeting.setDescription(meetingDTO.getDescription());
        meeting.setStartTime(meetingDTO.getStartTime());
        meeting.setEndTime(meetingDTO.getEndTime());
        meeting.setMeetingType(meetingType);
        meeting.setStatus(meetingDTO.getStatus());

        // Remove existing details
        meeting.setOnlineMeetingDetail(null);
        meeting.setOfflineMeetingDetail(null);

        if ("ONLINE".equals(meetingType.getName())) {
            OnlineMeetingDetail onlineDetail = new OnlineMeetingDetail();
            onlineDetail.setMeeting(meeting);
            onlineDetail.setOnlineLink(meetingDTO.getOnlineLink());
            onlineDetail.setPlatform(meetingDTO.getPlatform());
            meeting.setOnlineMeetingDetail(onlineDetail);
        } else if ("OFFLINE".equals(meetingType.getName())) {
            OfflineMeetingDetail offlineDetail = new OfflineMeetingDetail();
            offlineDetail.setMeeting(meeting);
            offlineDetail.setRoomLocation(meetingDTO.getRoomLocation());
            offlineDetail.setEquipmentNeeded(meetingDTO.getEquipmentNeeded());
            offlineDetail.setQuantity(meetingDTO.getQuantity());
            meeting.setOfflineMeetingDetail(offlineDetail);
        }

        return meetingRepository.save(meeting);
    }

    public Optional<Meeting> getMeetingById(Long id) {
        return meetingRepository.findById(id);
    }

    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }

    public List<Meeting> getMeetingsByMeetingType(Long meetingTypeId) {
        MeetingType meetingType = meetingTypeRepository.findById(meetingTypeId)
                .orElseThrow(() -> new RuntimeException("Meeting type not found"));
        return meetingRepository.findByMeetingType(meetingType);
    }

    @Transactional
    public Meeting addMeetingDetails(Long id, MeetingDTO meetingDTO) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        MeetingType meetingType = meeting.getMeetingType();
        if ("ONLINE".equals(meetingType.getName())) {
            OnlineMeetingDetail onlineDetail = meeting.getOnlineMeetingDetail();
            if (onlineDetail == null) {
                onlineDetail = new OnlineMeetingDetail();
                onlineDetail.setMeeting(meeting);
            }
            onlineDetail.setOnlineLink(meetingDTO.getOnlineLink());
            onlineDetail.setPlatform(meetingDTO.getPlatform());
            meeting.setOnlineMeetingDetail(onlineDetail);
            meeting.setOfflineMeetingDetail(null);
        } else if ("OFFLINE".equals(meetingType.getName())) {
            OfflineMeetingDetail offlineDetail = meeting.getOfflineMeetingDetail();
            if (offlineDetail == null) {
                offlineDetail = new OfflineMeetingDetail();
                offlineDetail.setMeeting(meeting);
            }
            offlineDetail.setRoomLocation(meetingDTO.getRoomLocation());
            offlineDetail.setEquipmentNeeded(meetingDTO.getEquipmentNeeded());
            offlineDetail.setQuantity(meetingDTO.getQuantity());
            meeting.setOfflineMeetingDetail(offlineDetail);
            meeting.setOnlineMeetingDetail(null);
        } else {
            throw new RuntimeException("Unsupported meeting type for adding details");
        }

        return meetingRepository.save(meeting);
    }

    @Transactional
    public void deleteMeeting(Long id) {
        meetingRepository.deleteById(id);
    }
    @Transactional
    public List<Meeting> getMeetingsWithinNext24Hours() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next24Hours = now.plusHours(24);
        return meetingRepository.findByStartTimeBetween(now, next24Hours);
}

}