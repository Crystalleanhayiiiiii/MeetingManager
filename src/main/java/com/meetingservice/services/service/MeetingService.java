// package com.meetingservice.services.service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.meetingservice.models.Meeting;
// import com.meetingservice.repository.MeetingRepository;

// //Service to Get Meeting from Database
// @Service
// public class MeetingService {

// private final MeetingRepository meetingRepository;

// // @Autowired
// public MeetingService(MeetingRepository meetingRepository) {
// this.meetingRepository = meetingRepository;
// }

// // Retrieve a meeting by ID from the database
// public Meeting getMeetingById(Long id) {
// return meetingRepository.findById(id).orElse(null); // Returns null if not
// found
// }
// }