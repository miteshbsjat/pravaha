package com.example.event;

public record OrderEvent(String orderId, String status, double amount) {}

