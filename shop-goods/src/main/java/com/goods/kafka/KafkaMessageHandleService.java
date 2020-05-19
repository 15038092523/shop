package com.goods.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageHandleService {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaMessageHandleService.class);

    @KafkaListener(topics = {"${spring.kafka.topics.topic1}"})
    public void receive(String message) {
        LOG.info("接收到消息：" + message);
    }
}
