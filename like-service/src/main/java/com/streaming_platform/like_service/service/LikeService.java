package com.streaming_platform.like_service.service;

import com.streaming_platform.like_service.model.Like;
import com.streaming_platform.like_service.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class LikeService {

    @Autowired
    private LikeRepository repository;

    public void like(String userId, String videoId){
        if (repository.existsByUserIdAndVideoId(userId, videoId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already Like video");
        }

        Like like = Like.builder()
                .userId(userId)
                .videoId(videoId)
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(like);
    }

    public void unlike(String userId, String videoId){
        repository.deleteByUserIdAndVideoId(userId, videoId);
    }

    public long getCount(String videoId){
        return repository.countByVideoId(videoId);
    }

    public boolean isLiked(String userId, String videoId){
        return repository.existsByUserIdAndVideoId(userId, videoId);
    }
}
