package com.meetingservice.services;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.meetingservice.event.MeetingEvent;

@Service
@Component
public class MeetingProducerService {
    private static final String TOPIC = "meeting-topic";
    // private final KafkaTemplate<String, Meeting> kafkaTemplateObject;

    // @Autowired
    // public MeetingProducerService(
    // @Qualifier("meetingKafkaTemplate") KafkaTemplate<String, Meeting>
    // kafkaTemplateObject) {
    // this.kafkaTemplateObject = kafkaTemplateObject;
    // }

    // public void sendMeeting(String key, Meeting meeting) {

    // kafkaTemplateObject.send(TOPIC, key, meeting);
    // }
    private final KafkaTemplate<String, Object> kafkaTemplate;

    // @Autowired
    public MeetingProducerService(@Qualifier("meetingKafkaTemplate") KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(String key, MeetingEvent event) {
        kafkaTemplate.send(TOPIC, key, event);
    }
}