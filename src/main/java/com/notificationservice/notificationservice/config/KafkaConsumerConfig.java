package com.notificationservice.notificationservice.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.notificationservice.notificationservice.event.MeetingEvent;
// import com.notificationservice.notificationservice.models.Meeting;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    // @Bean
    // public ConsumerFactory<String, String> consumerFactory() {
    // Map<String, Object> configProps = new HashMap<>();
    // configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    // configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
    // configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
    // StringDeserializer.class);
    // configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
    // StringDeserializer.class);
    // return new DefaultKafkaConsumerFactory<>(configProps);
    // }

    // @Bean
    // public ConcurrentKafkaListenerContainerFactory<String, String>
    // kafkaListenerContainerFactory() {
    // ConcurrentKafkaListenerContainerFactory<String, String> factory = new
    // ConcurrentKafkaListenerContainerFactory<>();
    // factory.setConsumerFactory(consumerFactory());
    // return factory;
    // }

    // ConsumerFactory cho Meeting object (JSON deserialization)
    // @Bean
    // public ConsumerFactory<String, Meeting> meetingConsumerFactory() {
    // Map<String, Object> props = new HashMap<>();
    // props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    // props.put(ConsumerConfig.GROUP_ID_CONFIG, "meeting");
    // props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
    // StringDeserializer.class);
    // props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
    // JsonDeserializer.class);
    // props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    // return new DefaultKafkaConsumerFactory<>(
    // props,
    // new StringDeserializer(),
    // new JsonDeserializer<>(Meeting.class, false));
    // }

    // @Bean
    // public ConcurrentKafkaListenerContainerFactory<String, Meeting>
    // meetingKafkaListenerContainerFactory() {
    // ConcurrentKafkaListenerContainerFactory<String, Meeting> factory = new
    // ConcurrentKafkaListenerContainerFactory<>();
    // factory.setConsumerFactory(meetingConsumerFactory());
    // return factory;
    // }

    @Bean
    public ConsumerFactory<String, MeetingEvent> meetingConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "notification");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, MeetingEvent.class.getName());
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean(name = "meetingKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, MeetingEvent> meetingKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MeetingEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(meetingConsumerFactory());
        factory.setCommonErrorHandler(new DefaultErrorHandler()); // skip bản ghi bẩn
        return factory;
    }

}
