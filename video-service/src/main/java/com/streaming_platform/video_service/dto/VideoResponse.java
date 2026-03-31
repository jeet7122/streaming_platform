package com.streaming_platform.video_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing the response returned to the client
 * after video-related operations (e.g., upload, fetch).
 *
 * <p>This object is used to expose only the necessary video information
 * to API consumers, hiding internal implementation details.
 *
 * <p><b>Key Responsibilities:</b>
 * <ul>
 *     <li>Provide video metadata to clients</li>
 *     <li>Represent current processing state of the video</li>
 * </ul>
 *
 * <p><b>Fields:</b>
 * <ul>
 *     <li>{@code id} - Unique identifier of the video</li>
 *     <li>{@code title} - Title of the video</li>
 *     <li>{@code status} - Current processing status (e.g., UPLOADED, PROCESSING, READY, FAILED)</li>
 * </ul>
 *
 * <p><b>Usage:</b>
 * <ul>
 *     <li>Returned from controller endpoints as API response</li>
 *     <li>Built using {@link Builder} pattern for flexibility</li>
 * </ul>
 *
 * <p><b>Note:</b>
 * <ul>
 *     <li>Status is typically driven by asynchronous processing pipelines</li>
 *     <li>Additional fields (e.g., playback URL, thumbnails) can be added in future versions</li>
 * </ul>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoResponse {

    /**
     * Unique identifier of the video.
     */
    private String id;

    /**
     * Title of the video.
     */
    private String title;

    /**
     * Current processing status of the video.
     */
    private String status;
}