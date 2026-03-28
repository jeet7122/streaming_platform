package com.streaming_platform.comment_service.controller;

import com.streaming_platform.comment_service.dto.CommentRequest;
import com.streaming_platform.comment_service.dto.CommentResponse;
import com.streaming_platform.comment_service.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    private String getUserId(HttpServletRequest request){
        String userId = request.getHeader("X-User-ID");
        if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No User Id");
        return userId;
    }

    @PostMapping("/{videoId}")
    public ResponseEntity<?> addComment(@PathVariable String videoId, @RequestBody CommentRequest cr, HttpServletRequest httpRequest){
        String userId = getUserId(httpRequest);
        return ResponseEntity.ok(commentService.addComment(userId, videoId, cr));
    }

    @GetMapping("/{videoId}")
    public ResponseEntity<List<CommentResponse>> getAllComments(@PathVariable String videoId){
        return ResponseEntity.ok(commentService.getComments(videoId));
    }
}
