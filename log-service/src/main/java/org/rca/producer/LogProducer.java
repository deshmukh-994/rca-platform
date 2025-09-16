package org.rca.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LogProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;

    public LogProducer(KafkaTemplate<String, String> kafkaTemplate,
                       @Value("${log.topic:logs}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void send(String key, String line) {
        kafkaTemplate.send(topic, key, line);
    }
}