package com.streaming_platform.video_service.kafka;

import com.streaming_platform.events.VideoUploadEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VideoEventProducer {

    private final KafkaTemplate<String, VideoUploadEvent> kafkaTemplate;

    public void publish(VideoUploadEvent e){
        kafkaTemplate.send("video.uploaded", e.getVideoId(), e);
    }
}
