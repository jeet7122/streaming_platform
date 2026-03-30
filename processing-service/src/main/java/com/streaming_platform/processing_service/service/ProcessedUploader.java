package com.streaming_platform.processing_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;

@Component
@RequiredArgsConstructor
public class ProcessedUploader {

    @Value("${R2.bucket}")
    private String bucket;

    private final S3Client s3Client;
    public void uploadFolder(String folderPath, String videoId) {

        File folder = new File(folderPath);

        File[] files = folder.listFiles();

        if (files == null || files.length == 0) {
            throw new RuntimeException("Folder is empty: " + folderPath);
        }

        for (File f : files) {

            System.out.println("Uploading file: " + f.getName()); // 🔥

            String key = "processed/" + videoId + "/" + f.getName();

            try {
                s3Client.putObject(
                        PutObjectRequest.builder()
                                .bucket(bucket)
                                .key(key)
                                .build(),
                        RequestBody.fromFile(f)
                );

                System.out.println("Uploaded: " + key); // 🔥

            } catch (Exception e) {
                e.printStackTrace(); // 🔥 VERY IMPORTANT
            }
        }
    }
}
