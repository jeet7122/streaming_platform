package com.streaming_platform.upload_service.controller;

import com.streaming_platform.upload_service.client.VideoClient;
import com.streaming_platform.upload_service.service.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * REST controller responsible for handling file upload requests.
 *
 * <p>This controller:
 * <ul>
 *     <li>Accepts multipart file uploads from clients</li>
 *     <li>Stores files using {@link StorageService}</li>
 *     <li>Notifies Video Service via {@link VideoClient}</li>
 * </ul>
 *
 * <p><b>Base Path:</b> /api/upload
 *
 * <p><b>Authentication:</b>
 * <ul>
 *     <li>Relies on {@code X-USER-ID} header provided by API Gateway</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {

    /**
     * Service responsible for storing uploaded files (e.g., S3/R2).
     */
    @Autowired
    private final StorageService storageService;

    /**
     * Client used to communicate with Video Service.
     */
    private final VideoClient videoClient;

    /**
     * Endpoint to upload a video file.
     *
     * <p>This endpoint:
     * <ul>
     *     <li>Validates user identity from request header</li>
     *     <li>Uploads file to storage</li>
     *     <li>Sends metadata to Video Service for further processing</li>
     * </ul>
     *
     * @param title       title of the video
     * @param file        multipart file uploaded by the client
     * @param description description of the video
     * @param request     HTTP request (used to extract headers)
     * @return success message or unauthorized response
     */
    @PostMapping
    public ResponseEntity<?> upload(
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String description,
            HttpServletRequest request
    ) {

        // Extract user ID from request header (injected by API Gateway)
        String userId = request.getHeader("X-USER-ID");

        // Validate presence of user ID
        if (userId == null) {
            return ResponseEntity.status(401).body("Unauthorized Request!");
        }

        // Upload file to storage service and obtain file key / URL
        String fileKey = storageService.upload(file);

        // Log uploaded file reference (replace with proper logging in production)
        System.out.println("SENDING FILE URL: " + fileKey);

        // Notify Video Service with metadata for further processing
        videoClient.upload(title, description, fileKey, userId);

        // Return success response to client
        return ResponseEntity.ok(Map.of("message", "Video Uploaded!"));
    }
}