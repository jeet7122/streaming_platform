package com.streaming_platform.upload_service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Client component responsible for communicating with the Video Service.
 *
 * <p>This client is used to send video upload metadata from the Upload Service
 * to the Video Service after a file has been successfully uploaded (e.g., to S3 or local storage).
 *
 * <p><b>Responsibilities:</b>
 * <ul>
 *     <li>Construct HTTP request payload for video creation</li>
 *     <li>Forward user identity via headers</li>
 *     <li>Invoke Video Service API endpoint</li>
 * </ul>
 *
 * <p><b>Configuration:</b>
 * <ul>
 *     <li>{@code video-service-url} should point to the Video Service endpoint (e.g., /api/videos/upload)</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class VideoClient {

    /**
     * RestTemplate used for making HTTP calls to external services.
     */
    private final RestTemplate restTemplate;

    /**
     * Base URL of the Video Service.
     * Injected from application configuration.
     */
    @Value("${video-service-url}")
    private String videoServiceUrl;

    /**
     * Sends a video upload request to the Video Service.
     *
     * <p>This method:
     * <ul>
     *     <li>Builds request body containing video metadata</li>
     *     <li>Sets user identity in request headers</li>
     *     <li>Performs HTTP POST call to Video Service</li>
     * </ul>
     *
     * @param title       title of the video
     * @param description description of the video
     * @param fileUrl     URL of the uploaded raw video file
     * @param userId      ID of the user uploading the video
     */
    public void upload(String title, String description, String fileUrl, String userId) {

        // Construct request body with video metadata
        Map<String, String> body = new HashMap<>();
        body.put("title", title);
        body.put("description", description);
        body.put("rawVideoUrl", fileUrl);

        // Set headers including user identity (propagated from API Gateway)
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-USER-ID", userId);

        // Wrap body and headers into HttpEntity
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        // Send POST request to Video Service
        restTemplate.postForEntity(videoServiceUrl, request, String.class);
    }
}