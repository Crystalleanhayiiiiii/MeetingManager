package com.meetingservice.services;

import java.util.UUID;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class RoomBookingPublisher {
    private static final String TOPIC = "room.booking.commands";
    private final KafkaTemplate<String, Object> kafka;

    public RoomBookingPublisher(KafkaTemplate<String, Object> kafka) {
        this.kafka = kafka;
    }

    public void publishBookingRequest(Long roomId, Map<String, Object> payload) {
        String key = String.valueOf(roomId);
        String correlationId = UUID.randomUUID().toString();

        ProducerRecord<String, Object> rec = new ProducerRecord<>(TOPIC, key, payload);
        rec.headers().add("eventType", "RoomBookingRequested".getBytes());
        rec.headers().add("correlationId", correlationId.getBytes());

        kafka.send(rec);
    }
}
