package com.example.producer.controller;

import com.example.producer.service.OrderProducerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

    private final OrderProducerService producerService;

    public ProducerController(OrderProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/api/orders")
    public String createOrder(@RequestParam double amount) {
        producerService.sendOrderEvent(amount);
        return "Order event published successfully";
    }
}