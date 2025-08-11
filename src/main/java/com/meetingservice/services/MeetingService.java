// com.meetingservice.services.MeetingService.java
package com.meetingservice.services;

import com.meetingservice.DTO.*;
import com.meetingservice.enums.*;
//import com.meetingservice.services.MeetingProducerService;
import com.meetingservice.event.MeetingEvent;
import com.meetingservice.event.MeetingEventType;
import com.meetingservice.models.Meeting;
import com.meetingservice.models.MeetingParticipant;

import com.meetingservice.repository.MeetingParticipantRepository;
import com.meetingservice.repository.MeetingRepository;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeetingService {

    private final MeetingRepository meetingRepo;
    private final MeetingParticipantRepository participantRepo;
    private final MeetingProducerService eventProducer;

    public MeetingService(MeetingRepository meetingRepo,
            MeetingParticipantRepository participantRepo,
            MeetingProducerService eventProducer) {
        this.meetingRepo = meetingRepo;
        this.participantRepo = participantRepo;
        this.eventProducer = eventProducer;
    }

    private void validateCreate(CreateMeetingRequest r) {
        if (r.getTitle() == null || r.getTitle().isBlank())
            throw new IllegalArgumentException("title is required");
        if (r.getOrganizerId() == null)
            throw new IllegalArgumentException("organizerId is required");
        if (r.getType() == null)
            throw new IllegalArgumentException("type is required");
        if (r.getStartTime() == null || r.getEndTime() == null || !r.getEndTime().isAfter(r.getStartTime()))
            throw new IllegalArgumentException("invalid start/end time");
        if (r.getType() == MeetingType.ONLINE) {
            if (r.getOnlinePlatform() == null || r.getOnlineLink() == null || r.getOnlineLink().isBlank())
                throw new IllegalArgumentException("onlinePlatform & onlineLink are required for ONLINE meeting");
        } else {
            if (r.getRoomId() == null)
                throw new IllegalArgumentException("roomId is required for OFFLINE meeting");
        }
    }

    private void applyUpdate(Meeting m, UpdateMeetingRequest r) {
        if (r.getTitle() != null)
            m.setTitle(r.getTitle());
        if (r.getDescription() != null)
            m.setDescription(r.getDescription());
        if (r.getType() != null)
            m.setType(r.getType());
        if (r.getStartTime() != null)
            m.setStartTime(r.getStartTime());
        if (r.getEndTime() != null)
            m.setEndTime(r.getEndTime());

        if (m.getType() == MeetingType.ONLINE) {
            if (r.getOnlinePlatform() != null)
                m.setOnlinePlatform(r.getOnlinePlatform());
            if (r.getOnlineLink() != null)
                m.setOnlineLink(r.getOnlineLink());
            m.setRoomId(null);
            if (r.getDeviceId() != null)
                m.setDeviceIds(new HashSet<>()); // clear
            if (m.getStatus() == MeetingStatus.PENDING_APPROVAL)
                m.setStatus(MeetingStatus.UPCOMING);
        } else {
            if (r.getRoomId() != null)
                m.setRoomId(r.getRoomId());
            if (r.getDeviceId() != null)
                m.setDeviceIds(new HashSet<>(r.getDeviceId()));
            m.setStatus(MeetingStatus.PENDING_APPROVAL); // thay đổi OFFLINE cần chờ duyệt
            m.setOnlineLink(null);
            m.setOnlinePlatform(null);
        }
    }

    @Transactional
    public Meeting create(CreateMeetingRequest r) {
        validateCreate(r);
        Meeting m = new Meeting();
        m.setTitle(r.getTitle());
        m.setDescription(r.getDescription());
        m.setOrganizerId(r.getOrganizerId());
        m.setType(r.getType());
        m.setStartTime(r.getStartTime());
        m.setEndTime(r.getEndTime());
        m.setOnlinePlatform(r.getOnlinePlatform());
        m.setOnlineLink(r.getOnlineLink());
        m.setRoomId(r.getRoomId());
        m.setDeviceIds(r.getDeviceIds() == null ? new HashSet<>() : new HashSet<>(r.getDeviceIds()));
        m.setStatus(r.getType() == MeetingType.OFFLINE ? MeetingStatus.PENDING_APPROVAL : MeetingStatus.UPCOMING);

        meetingRepo.save(m);

        // organizer là participant
        MeetingParticipant organizer = new MeetingParticipant();
        organizer.setMeeting(m);
        organizer.setUserId(r.getOrganizerId());
        organizer.setRole(ParticipantRole.ORGANIZER);
        participantRepo.save(organizer);

        publishEvent(MeetingEventType.CREATED, m, null);
        return m;
    }

    @Transactional
    public Meeting update(Long meetingId, Long requesterId, UpdateMeetingRequest r) {
        Meeting m = requireMeeting(meetingId);
        ensureOrganizer(m, requesterId);
        if (m.getStatus() == MeetingStatus.CANCELLED)
            throw new IllegalStateException("meeting is cancelled");
        if (m.getStatus() == MeetingStatus.COMPLETED)
            throw new IllegalStateException("meeting is completed");

        applyUpdate(m, r);
        meetingRepo.save(m);
        publishEvent(MeetingEventType.UPDATED, m, null);
        return m;
    }

    @Transactional
    public void cancel(Long meetingId, Long organizerId, String reason) {
        Meeting m = requireMeeting(meetingId);
        ensureOrganizer(m, organizerId);
        if (m.getStatus() == MeetingStatus.CANCELLED)
            return;

        m.setStatus(MeetingStatus.CANCELLED);
        m.setCancelReason(reason);
        meetingRepo.save(m);

        publishEvent(MeetingEventType.CANCELLED, m, reason);
    }

    @Transactional
    public List<Long> addParticipants(Long meetingId, Long organizerId, List<Long> userIds) {
        Meeting m = requireMeeting(meetingId);
        ensureOrganizer(m, organizerId);
        List<Long> added = new ArrayList<>();
        if (userIds != null) {
            for (Long uid : new HashSet<>(userIds)) {
                if (uid == null)
                    continue;
                if (participantRepo.existsByMeetingIdAndUserId(meetingId, uid))
                    continue;
                MeetingParticipant p = new MeetingParticipant();
                p.setMeeting(m);
                p.setUserId(uid);
                p.setRole(ParticipantRole.ATTENDEE);
                participantRepo.save(p);
                added.add(uid);
            }
        }
        publishEvent(MeetingEventType.UPDATED, m, null);
        return added;
    }

    @Transactional
    public void approve(Long meetingId, Long adminId) {
        Meeting m = requireMeeting(meetingId);
        if (m.getType() != MeetingType.OFFLINE)
            throw new IllegalStateException("only OFFLINE meeting requires approval");
        if (m.getStatus() != MeetingStatus.PENDING_APPROVAL)
            return;

        // TODO: validate với RoomService
        m.setStatus(MeetingStatus.UPCOMING);
        meetingRepo.save(m);
        publishEvent(MeetingEventType.UPDATED, m, null);
    }

    private Meeting requireMeeting(Long id) {
        return meetingRepo.findById(id).orElseThrow(() -> new NoSuchElementException("meeting not found"));
    }

    private void ensureOrganizer(Meeting m, Long userId) {
        if (!Objects.equals(m.getOrganizerId(), userId))
            throw new SecurityException("only organizer can perform this action");
    }

    private void publishEvent(MeetingEventType type, Meeting m, String cancelReason) {
        List<Long> participantIds = participantRepo.findByMeetingId(m.getId())
                .stream().map(MeetingParticipant::getUserId).collect(Collectors.toList());

        MeetingEvent ev = new MeetingEvent();
        ev.setEventType(type);
        ev.setMeetingId(m.getId());
        ev.setTitle(m.getTitle());
        ev.setOrganizerId(m.getOrganizerId());
        ev.setType(m.getType());
        ev.setStatus(m.getStatus());
        ev.setStartTime(m.getStartTime());
        ev.setEndTime(m.getEndTime());
        ev.setOnlineLink(m.getOnlineLink());
        ev.setOnlinePlatform(m.getOnlinePlatform());
        ev.setRoomId(m.getRoomId());
        ev.setParticipantUserIds(participantIds);
        ev.setCancelReason(cancelReason);
        ev.setEventTime(LocalDateTime.now());

        // Key = meetingId để đảm bảo ordering trong partition
        eventProducer.publish(String.valueOf(m.getId()), ev);
    }

    public Page<Meeting> getUpcomingAll(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").ascending());
        return meetingRepo.findUpcomingForUser(userId, MeetingStatus.UPCOMING, LocalDateTime.now(), pageable);
        // nếu muốn tính cả PENDING_APPROVAL cho offline thì đổi điều kiện ở repo
    }

    public Page<Meeting> getUpcomingOrganizer(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").ascending());
        return meetingRepo.findUpcomingOrganizer(userId, MeetingStatus.UPCOMING, LocalDateTime.now(), pageable);
    }

    public Page<Meeting> getUpcomingAttendee(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").ascending());
        return meetingRepo.findUpcomingAttendee(userId, MeetingStatus.UPCOMING, LocalDateTime.now(), pageable);
    }

    public Page<Meeting> getCompletedAll(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("endTime").descending());
        return meetingRepo.findCompletedForUser(userId, MeetingStatus.COMPLETED, LocalDateTime.now(), pageable);
    }

    public List<Meeting> getAllByUser(Long userId) {
        return meetingRepo.findAllMeetingForUser(userId);
    }

}
