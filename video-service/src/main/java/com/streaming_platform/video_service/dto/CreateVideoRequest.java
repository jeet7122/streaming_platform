package com.streaming_platform.video_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a request to create/upload a video.
 *
 * <p>This object is typically received from the client (UI/API consumer)
 * when a user uploads or registers a new video in the system.
 *
 * <p><b>Responsibilities:</b>
 * <ul>
 *     <li>Carry video metadata from client to backend service</li>
 *     <li>Encapsulate request payload in a structured format</li>
 * </ul>
 *
 * <p><b>Fields:</b>
 * <ul>
 *     <li>{@code title} - Title of the video</li>
 *     <li>{@code description} - Description or summary of the video</li>
 *     <li>{@code rawVideoUrl} - URL/location of the uploaded raw video file</li>
 * </ul>
 *
 * <p><b>Usage:</b>
 * <ul>
 *     <li>Used in controller layer as request body</li>
 *     <li>Mapped to internal models or entities for processing</li>
 * </ul>
 *
 * <p><b>Note:</b>
 * <ul>
 *     <li>No validation annotations are present (can be added using javax/jakarta validation)</li>
 *     <li>Assumes rawVideoUrl is already uploaded and accessible</li>
 * </ul>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateVideoRequest {

    /**
     * Title of the video.
     */
    private String title;

    /**
     * Description providing details about the video content.
     */
    private String description;

    /**
     * URL pointing to the raw uploaded video file (e.g., S3, local storage).
     */
    private String rawVideoUrl;
}