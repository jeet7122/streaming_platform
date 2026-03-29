package com.streaming_platform.upload_service.controller;

import com.streaming_platform.upload_service.client.VideoClient;
import com.streaming_platform.upload_service.service.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {
    private final StorageService storageService;
    private final VideoClient videoClient;

    @PostMapping
    public ResponseEntity<?> upload(
            @RequestParam String title,
            @RequestParam("file") MultipartFile file,
            @RequestParam String description,
            HttpServletRequest request
            ){
        String userId = request.getHeader("X-USER-ID");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized Request!");

        String fileKey = storageService.upload(file);

        videoClient.upload(title, description, fileKey, userId);

        return ResponseEntity.ok(Map.of("message", "Video Uploaded!"));
    }
}
