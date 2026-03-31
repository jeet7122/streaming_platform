package com.streaming_platform.processing_service.kafka;

import com.streaming_platform.events.VideoUploadEvent;
import com.streaming_platform.processing_service.client.VideoClient;
import com.streaming_platform.processing_service.service.ProcessedUploader;
import com.streaming_platform.processing_service.service.RecoveryService;
import com.streaming_platform.processing_service.service.VideoProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class VideoProcessingConsumer {
    private final RecoveryService downloader;
    private final VideoProcessingService videoProcessingService;
    private final ProcessedUploader uploader;
    private final VideoClient client;

    @Value("${R2.public-endpoint}")
    private String r2BaseUrl;

    @KafkaListener(topics = "video.uploaded", containerFactory = "kafkaListenerContainerFactory")
    public void handle(VideoUploadEvent e) throws InterruptedException {


        if (e.getRawVideoUrl() == null) throw new IllegalArgumentException("Invalid Event: Video URL is null");
        // 1. DOWNLOADING FILE FROM CLOUD
        Path inputFile = downloader.download(e.getRawVideoUrl());

        // 2. PROCESSING FILES { FFMPEG }
        String outputDIR = videoProcessingService.process(inputFile, e.getVideoId());

        // 3. UPLOADED PROCESSED FILES
        System.out.println("Uploading folder!");
        uploader.uploadFolder(outputDIR, e.getVideoId());

        // 4. UPDATE VIDEO-SERVICE
        String manifestURL = r2BaseUrl + "/processed/" + e.getVideoId() + "/" + e.getVideoId() + "/master.m3u8";
        Thread.sleep(2000);
        client.updateVideo(e.getVideoId(), manifestURL);


    }
}
