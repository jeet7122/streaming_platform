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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;

    @PostMapping("/upload")
    public ResponseEntity<VideoResponse> publishVideo(@RequestBody CreateVideoRequest req, HttpServletRequest httpRequest){
        try{
            String userId = httpRequest.getHeader("X-USER-ID");
            return ResponseEntity.ok(videoService.publishVideo(req, userId));
        } catch (Exception e) {
            e.printStackTrace(); // 🔥 THIS IS IMPORTANT
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<Video>> getUserVideos(HttpServletRequest req){
        String userId = req.getHeader("X-USER-ID");
        return ResponseEntity.ok(videoService.getUserVideos(userId));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> complete(@PathVariable String id, @RequestBody Map<String, String> body){
        String response = videoService.markAsReady(id, body.get("manifestUrl"));
        return ResponseEntity.ok(response);
    }


}
