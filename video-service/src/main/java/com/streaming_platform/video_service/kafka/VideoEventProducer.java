package com.streaming_platform.video_service.kafka;

import com.streaming_platform.events.VideoUploadEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka producer responsible for publishing {@link VideoUploadEvent} events.
 *
 * <p>This producer sends video upload events to Kafka so that downstream services
 * (e.g., video processing, notifications) can consume and process them asynchronously.
 *
 * <p><b>Key Responsibilities:</b>
 * <ul>
 *     <li>Publish video upload events to Kafka topic</li>
 *     <li>Retry sending events in case of transient failures</li>
 *     <li>Log retry attempts and failures for observability</li>
 * </ul>
 *
 * <p><b>Important Notes:</b>
 * <ul>
 *     <li>Uses {@code videoId} as the message key to ensure ordering per video</li>
 *     <li>Retries are application-level and limited to a fixed number of attempts</li>
 *     <li>KafkaTemplate.send() is asynchronous; exceptions may not be thrown immediately</li>
 * </ul>
 *
 * <p><b>Future Improvements:</b>
 * <ul>
 *     <li>Use callbacks or blocking send (.get()) for reliable retry handling</li>
 *     <li>Externalize topic name to configuration</li>
 *     <li>Integrate Outbox Pattern for guaranteed delivery</li>
 * </ul>
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class VideoEventProducer {

    /**
     * Kafka template used to publish messages to Kafka topics.
     */
    private final KafkaTemplate<String, VideoUploadEvent> kafkaTemplate;

    /**
     * Publishes a {@link VideoUploadEvent} to Kafka with retry logic.
     *
     * <p>This method attempts to send the event up to a maximum number of retries
     * in case of failure. If all attempts fail, a RuntimeException is thrown.
     *
     * @param e the video upload event to be published
     * @throws RuntimeException if the event could not be published after retries
     */
    public void publish(VideoUploadEvent e) {

        int attempt = 0;
        int maxAttempts = 3;

        // Retry loop for handling transient failures
        while (attempt < maxAttempts) {
            try {
                // Send event to Kafka topic using videoId as key (ensures ordering per video)
                kafkaTemplate.send("video.uploaded", e.getVideoId(), e);

                // Exit on successful send
                return;

            } catch (Exception ex) {
                attempt++;

                // Log retry attempt with context
                log.warn("Retry {}/{} for videoId={}", attempt, maxAttempts, e.getVideoId());

                // If max retries reached, propagate failure
                if (attempt == maxAttempts) {
                    log.error("Failed to publish event after {} attempts for videoId={}",
                            maxAttempts, e.getVideoId(), ex);

                    throw new RuntimeException("Failed to publish event", ex);
                }
            }
        }
    }
}