package com.streaming_platform.video_service.service;

import com.streaming_platform.events.VideoUploadEvent;
import com.streaming_platform.video_service.dto.CreateVideoRequest;
import com.streaming_platform.video_service.dto.VideoResponse;
import com.streaming_platform.video_service.kafka.VideoEventProducer;
import com.streaming_platform.video_service.model.Status;
import com.streaming_platform.video_service.model.Video;
import com.streaming_platform.video_service.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository repository;
    private final VideoEventProducer producer;

    public VideoResponse publishVideo(CreateVideoRequest req, String userId){
        Video video = Video.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .userId(userId)
                .status(Status.UPLOADING)
                .build();
        repository.save(video);

        producer.publish(new VideoUploadEvent(video.getId(), userId));

        return new VideoResponse(video.getId(), video.getTitle(), video.getStatus().toString());
    }


    public List<Video> getUserVideos(String userId){
        return repository.findByUserId(userId);
    }
}
