package com.meetingservice.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "my_topic", groupId = "meeting")
    public void consume(String message) {
        System.out.println("Message received: " + message);
    }
}
