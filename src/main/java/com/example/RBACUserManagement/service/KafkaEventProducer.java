package com.example.RBACUserManagement.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;


@Slf4j
@Service
public class KafkaEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaEventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendUserEvent(String topic, Map<String, Object> payload) {
        try {
            log.info("preparing send to topic");
            String json = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(topic, json);
        } catch (Exception e) {
            // Log error. Kafka failure shouldn't break critical flows.
            System.err.println("Failed to publish Kafka event: " + e.getMessage());
        }
    }
}

