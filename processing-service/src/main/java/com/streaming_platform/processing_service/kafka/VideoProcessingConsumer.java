package com.streaming_platform.processing_service.kafka;

import com.streaming_platform.events.VideoUploadEvent;
import com.streaming_platform.processing_service.client.VideoClient;
import com.streaming_platform.processing_service.service.GarbageCollectionService;
import com.streaming_platform.processing_service.service.ProcessedUploader;
import com.streaming_platform.processing_service.service.RecoveryService;
import com.streaming_platform.processing_service.service.VideoProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@RequiredArgsConstructor
@Slf4j
public class VideoProcessingConsumer {
    private final RecoveryService downloader;
    private final VideoProcessingService videoProcessingService;
    private final ProcessedUploader uploader;
    private final VideoClient client;
    private final GarbageCollectionService gcs;

    @Value("${R2.public-endpoint}")
    private String r2BaseUrl;

    @KafkaListener(topics = "video.uploaded", containerFactory = "kafkaListenerContainerFactory")
    public void handle(VideoUploadEvent e) {
        Path inputFile = null;
        String outputDir = null;
        try {
            // DOWNLOAD
            inputFile = downloader.download(e.getRawVideoUrl());

            // PROCESS
            outputDir = videoProcessingService.process(inputFile, e.getVideoId());

            // UPLOAD PROCESSED FILES
            uploader.uploadFolder(outputDir, e.getVideoId());

            // UPDATE DATABASE
            String manifestUrl = r2BaseUrl + "/processed/" + e.getVideoId() + "/" + e.getVideoId() + "/master.m3u8";
            String thumbnailUrl = r2BaseUrl + "/processed/" + e.getVideoId() + "/" + e.getVideoId() + "/thumb.jpg";
            client.updateVideo(e.getVideoId(), manifestUrl, thumbnailUrl);


            // DELETE RAW VIDEO FILE FROM CLOUD STORAGE
            gcs.deleteFromCloudStorage(e.getRawVideoUrl());
        }
        catch (Exception ex){
            log.error("SOMETHING WENT WRONG WHILE PROCESSING: {}", ex.getMessage());
        }
        finally {
            gcs.cleanUp(inputFile, outputDir);
        }
    }
}
