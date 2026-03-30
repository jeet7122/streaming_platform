package com.streaming_platform.processing_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class RecoveryService {
    private final S3Client s3Client;

    @Value("${R2.bucket}")
    private String bucket;

    public Path download(String key){
        Path path = Paths.get("temp/" + key);
        System.out.println("DOWNLOADING KEY: " + key);

        try {
            Files.createDirectories(path.getParent());

            ResponseInputStream<GetObjectResponse> object = s3Client
                    .getObject(
                            GetObjectRequest.builder()
                                    .bucket(bucket)
                                    .key(key)
                                    .build()
                    );

            Files.copy(object, path, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to download video for processing " + key);
        }

        return path;
    }
}
