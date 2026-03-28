package com.streaming_platform.like_service.controller;

import com.streaming_platform.like_service.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService service;

    @PostMapping("/{videoId}")
    public ResponseEntity<String> like(@PathVariable String videoId, HttpServletRequest request){
        String userId = request.getHeader("X-USER-ID");
        service.like(userId, videoId);
        return ResponseEntity.ok("Liked");
    }


    @DeleteMapping("/{videoId}")
    public ResponseEntity<String> unlike(@PathVariable String videoId, HttpServletRequest request){
        String userId = request.getHeader("X-USER-ID");
        service.unlike(userId, videoId);
        return ResponseEntity.ok("Unliked");
    }

    @GetMapping("/{videoId}/count")
    public ResponseEntity<Long> likeCount(@PathVariable String videoId){
        return ResponseEntity.ok(service.getCount(videoId));
    }

    @GetMapping("/{videoId}/status")
    public ResponseEntity<Boolean> isLiked(@PathVariable String videoId, HttpServletRequest request){
        String userId = request.getHeader("X-USER-ID");
        return ResponseEntity.ok(service.isLiked(userId, videoId));
    }
}
