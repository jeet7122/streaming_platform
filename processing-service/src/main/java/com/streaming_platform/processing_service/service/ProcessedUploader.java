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
        uploadRecursive(folder, videoId, "");
    }

    private void uploadRecursive(File file, String videoId, String prefix) {

        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                uploadRecursive(f, videoId, prefix + file.getName() + "/");
            }
        } else {
            String key = "processed/" + videoId + "/" + prefix + file.getName();

            System.out.println("Uploading: " + key);

            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .build(),
                    RequestBody.fromFile(file)
            );
        }
    }
}
