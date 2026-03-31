package com.streaming_platform.video_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a video in the system.
 *
 * <p>This entity maps to the {@code videos} table in the database and stores
 * metadata related to uploaded videos and their processing lifecycle.
 *
 * <p><b>Responsibilities:</b>
 * <ul>
 *     <li>Persist video-related data</li>
 *     <li>Track processing status of the video</li>
 *     <li>Store references to raw and processed video resources</li>
 * </ul>
 *
 * <p><b>Lifecycle:</b>
 * <ul>
 *     <li>Video is created with status (e.g., UPLOADED)</li>
 *     <li>Processing service updates status (PROCESSING → READY/FAILED)</li>
 *     <li>Manifest URL is populated after successful processing</li>
 * </ul>
 *
 * <p><b>Table:</b> videos
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "videos")
public class Video {

    /**
     * Unique identifier for the video.
     * Generated using UUID strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /**
     * Title of the video.
     */
    private String title;

    /**
     * Description providing details about the video content.
     */
    private String description;

    /**
     * Identifier of the user who uploaded the video.
     * Typically extracted from JWT at the API Gateway.
     */
    private String userId;

    /**
     * Current processing status of the video.
     * Stored as STRING in database for readability.
     */
    @Enumerated(value = EnumType.STRING)
    private Status status;

    /**
     * URL pointing to the raw uploaded video file.
     * This is the source file used for processing.
     */
    private String rawVideoUrl;

    /**
     * URL pointing to the generated HLS/DASH manifest file.
     * Used for streaming the processed video.
     */
    private String manifestUrl;

    /**
     * Timestamp indicating when the video was created.
     */
    private LocalDateTime createdAt;
}