package com.streaming_platform.processing_service.config;

import com.streaming_platform.events.VideoUploadEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;

@Configuration
public class KafkaConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, VideoUploadEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, VideoUploadEvent> consumerFactory,
            DefaultErrorHandler handler
    ){
        ConcurrentKafkaListenerContainerFactory<String, VideoUploadEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(handler);
        return factory;
    }
}
