package com.example.producer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topic.name}")
    private String topicName;

    @Value("${kafka.topic.partitions}")
    private int partitions;

    @Bean
    public NewTopic orderEventsTopic() {
        return TopicBuilder.name(topicName)
                .partitions(partitions)
                .replicas(3) // Set to 3 in a real multi-broker production cluster
                .build();
    }
}