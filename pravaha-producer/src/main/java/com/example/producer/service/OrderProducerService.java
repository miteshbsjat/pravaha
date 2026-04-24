package com.example.producer.service;

import com.example.event.OrderEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderProducerService {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private final String topicName;

    public OrderProducerService(KafkaTemplate<String, OrderEvent> kafkaTemplate,
            @Value("${kafka.topic.name}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void sendOrderEvent(double amount) {
        OrderEvent event = new OrderEvent(UUID.randomUUID().toString(), "CREATED", amount);
        // Using orderId as the message key ensures events for the same order go to the
        // same partition
        kafkaTemplate.send(topicName, event.orderId(), event);
        System.out.println("Produced event: " + event);
    }
}