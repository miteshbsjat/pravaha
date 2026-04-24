package com.example.consumer.service;

import com.example.event.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventConsumer {

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeOrderEvent(OrderEvent event) {
        // In a production environment, implement idempotency checks here (e.g.,
        // checking DB for processed orderId)
        System.out.println("Consumed event via thread [" + Thread.currentThread().getName() + "]: " + event);
    }
}