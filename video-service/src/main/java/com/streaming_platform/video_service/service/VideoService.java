package com.streaming_platform.video_service.service;

import com.streaming_platform.events.VideoUploadEvent;
import com.streaming_platform.video_service.dto.CreateVideoRequest;
import com.streaming_platform.video_service.dto.VideoResponse;
import com.streaming_platform.video_service.kafka.VideoEventProducer;
import com.streaming_platform.video_service.model.Status;
import com.streaming_platform.video_service.model.Video;
import com.streaming_platform.video_service.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer responsible for handling business logic related to videos.
 *
 * <p>This service acts as an intermediary between the controller layer and
 * the persistence/event layers. It manages video creation, status updates,
 * and retrieval operations.
 *
 * <p><b>Key Responsibilities:</b>
 * <ul>
 *     <li>Create and persist video metadata</li>
 *     <li>Trigger asynchronous processing via Kafka events</li>
 *     <li>Update video status after processing</li>
 *     <li>Fetch user-specific videos</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class VideoService {

    /**
     * Repository for performing CRUD operations on Video entities.
     */
    private final VideoRepository repository;

    /**
     * Kafka producer for publishing video upload events.
     */
    private final VideoEventProducer producer;

    /**
     * Publishes a new video.
     *
     * <p>This method:
     * <ul>
     *     <li>Creates a new Video entity</li>
     *     <li>Persists it in the database</li>
     *     <li>Publishes a Kafka event for asynchronous processing</li>
     * </ul>
     *
     * @param req    request DTO containing video details
     * @param userId ID of the user uploading the video
     * @return response DTO containing video metadata
     */
    public VideoResponse publishVideo(CreateVideoRequest req, String userId) {

        // Log incoming raw video URL (use proper logging in production)
        System.out.println("REQUESTED URL: " + req.getRawVideoUrl());

        // Build Video entity with initial status
        Video video = Video.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .userId(userId)
                .status(Status.UPLOADED) // Initial state before processing
                .rawVideoUrl(req.getRawVideoUrl())
                .manifestUrl(null) // Not available until processing completes
                .build();

        // Persist video metadata in database
        repository.save(video);

        // Publish event to Kafka for asynchronous video processing
        producer.publish(new VideoUploadEvent(
                userId,
                video.getId(),
                video.getRawVideoUrl()
        ));

        // Return response DTO to client
        return new VideoResponse(
                video.getId(),
                video.getTitle(),
                video.getStatus().toString()
        );
    }

    /**
     * Marks a video as ready after processing is complete.
     *
     * <p>This method:
     * <ul>
     *     <li>Fetches the video from database</li>
     *     <li>Updates manifest URL (HLS/DASH)</li>
     *     <li>Updates timestamp</li>
     * </ul>
     *
     * @param videoId    unique identifier of the video
     * @param manifestUrl URL of the processed video manifest
     * @return confirmation message
     * @throws ResponseStatusException if video is not found
     */
    public String markAsReady(String videoId, String manifestUrl, String thumbnailUrl) {

        // Fetch existing video or throw 404 if not found
        Video existingVideo = repository.findById(videoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Video with video ID: " + videoId + " not found in DB"
                ));

        // Update processed video details
        existingVideo.setManifestUrl(manifestUrl);
        existingVideo.setThumbnailUrl(thumbnailUrl);
        existingVideo.setStatus(Status.READY);
        existingVideo.setCreatedAt(LocalDateTime.now()); // Update timestamp

        // Persist updated entity
        repository.save(existingVideo);

        return "SAVED VIDEO WITH MANIFEST URL!";
    }

    /**
     * Retrieves all videos uploaded by a specific user.
     *
     * @param userId unique identifier of the user
     * @return list of videos belonging to the user
     */
    public List<Video> getUserVideos(String userId) {
        return repository.findByUserId(userId);
    }


    public List<Video> search(String query) {
        return repository.search(query);
    }

    public List<Video> getFeed(int page, int size){
        return repository
                .findByStatus(Status.READY, PageRequest.of(page, size)).getContent();
    }

    public Video getById(String id){
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found!"));
    }
}