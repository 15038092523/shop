package com.goods.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageSendService {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaMessageSendService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topics.topic1}")
    private String topic;

    public void send() {
        String message = "Hello World---" + System.currentTimeMillis();
        LOG.info("topic=" + topic + ",message=" + message);
        kafkaTemplate.send(topic, message);
    }
}
