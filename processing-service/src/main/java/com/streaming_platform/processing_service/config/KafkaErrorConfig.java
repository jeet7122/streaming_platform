package com.streaming_platform.processing_service.config;

import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaErrorConfig {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Bean
    public DefaultErrorHandler errorHandler(){
        FixedBackOff backOff = new FixedBackOff(3000L, 3);

        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(
                        kafkaTemplate,
                        (record, exception) -> new TopicPartition(
                                record.topic() + ".dlq",
                                record.partition()
                        )
                );

        DefaultErrorHandler handler = new DefaultErrorHandler(recoverer, backOff);

        handler.addNotRetryableExceptions(IllegalArgumentException.class);
        handler.addRetryableExceptions(RuntimeException.class);

        handler.setRetryListeners(((record, ex, deliveryAttempt) -> {
            System.out.println("Retry #" + deliveryAttempt +
                    " | Event: " + record.value() +
                    " | Error: " + ex.getMessage());
        }));

        return handler;
    }
}
