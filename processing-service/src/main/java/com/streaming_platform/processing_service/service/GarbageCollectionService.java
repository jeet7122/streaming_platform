package com.streaming_platform.processing_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class GarbageCollectionService {

    @Autowired
    private S3Client s3Client;

    @Value("${R2.bucket}")
    private String bucket;

    public void cleanUp(Path inputFile, String outputDir){
        try {
            if (inputFile != null){
                Files.deleteIfExists(inputFile);
            }
            if (outputDir != null) deleteDirectory(new File(outputDir));
            System.out.println("🧹 Cleanup completed");
        }catch (Exception e){
            System.err.println("Cleanup failed: " + e.getMessage());
        }
    }

    private void deleteDirectory(File dir){
        if (dir == null || !dir.exists()) return;

        File[] files = dir.listFiles();
        if (files != null){
            for (File f : files){
                if (f.isDirectory()) deleteDirectory(f);
                else f.delete();
            }
        }
        dir.delete();
    }

    public void deleteFromCloudStorage(String key){
        s3Client.deleteObject(builder -> builder.bucket(bucket).key(key));
    }
}
