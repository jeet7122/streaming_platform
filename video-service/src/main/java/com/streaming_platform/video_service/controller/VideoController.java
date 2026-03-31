package com.streaming_platform.video_service.controller;

import com.streaming_platform.video_service.dto.CreateVideoRequest;
import com.streaming_platform.video_service.dto.VideoResponse;
import com.streaming_platform.video_service.model.Video;
import com.streaming_platform.video_service.service.VideoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller responsible for handling video-related API requests.
 *
 * <p>This controller exposes endpoints for:
 * <ul>
 *     <li>Uploading videos</li>
 *     <li>Fetching user-specific videos</li>
 *     <li>Marking videos as processed/ready</li>
 * </ul>
 *
 * <p><b>Base Path:</b> /api/videos
 *
 * <p><b>Authentication:</b>
 * <ul>
 *     <li>User identity is extracted from the {@code X-USER-ID} header</li>
 *     <li>This header is typically injected by the API Gateway after JWT validation</li>
 * </ul>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/videos")
public class VideoController {

    /**
     * Service layer handling video-related business logic.
     */
    private final VideoService videoService;

    /**
     * Endpoint to upload/publish a new video.
     *
     * <p>This endpoint:
     * <ul>
     *     <li>Accepts video metadata from client</li>
     *     <li>Extracts user identity from request header</li>
     *     <li>Delegates processing to service layer</li>
     * </ul>
     *
     * @param req         request body containing video details
     * @param httpRequest HTTP servlet request (used to extract headers)
     * @return response containing video metadata
     */
    @PostMapping("/upload")
    public ResponseEntity<VideoResponse> publishVideo(
            @RequestBody CreateVideoRequest req,
            HttpServletRequest httpRequest
    ) {
        try {
            // Extract user ID from gateway-injected header
            String userId = httpRequest.getHeader("X-USER-ID");

            // Delegate to service layer
            return ResponseEntity.ok(videoService.publishVideo(req, userId));

        } catch (Exception e) {
            // Log exception (replace with proper logging in production)
            e.printStackTrace(); // 🔥 Debugging aid during development

            // Re-throw exception to be handled globally
            throw e;
        }
    }

    /**
     * Endpoint to fetch all videos uploaded by the authenticated user.
     *
     * @param req HTTP servlet request (used to extract headers)
     * @return list of videos belonging to the user
     */
    @GetMapping
    public ResponseEntity<List<Video>> getUserVideos(HttpServletRequest req) {

        // Extract user ID from request header
        String userId = req.getHeader("X-USER-ID");

        // Fetch videos for the given user
        return ResponseEntity.ok(videoService.getUserVideos(userId));
    }

    /**
     * Endpoint to mark a video as processing complete.
     *
     * <p>This is typically called by a processing service after
     * video transcoding and manifest generation are completed.
     *
     * @param id   video ID
     * @param body request body containing manifest URL
     * @return confirmation response
     */
    @PutMapping("/internal/{id}/complete")
    public ResponseEntity<?> complete(
            @PathVariable("id") String id,
            @RequestBody Map<String, String> body
    ) {

        // Extract manifest URL from request body
        String manifestUrl = body.get("manifestUrl");

        // Update video status and manifest URL
        String response = videoService.markAsReady(id, manifestUrl);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Video>> search(@RequestParam("query") String query){
        return ResponseEntity.ok(videoService.search(query));
    }

    @GetMapping("/feed")
    public ResponseEntity<List<Video>> feed(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size
    ){
        return ResponseEntity.ok(videoService.getFeed(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Video> getVideoById(@PathVariable("id") String id){
        return ResponseEntity.ok(videoService.getById(id));
    }
}