package com.streaming_platform.comment_service.service;

import com.streaming_platform.comment_service.dto.CommentRequest;
import com.streaming_platform.comment_service.dto.CommentResponse;
import com.streaming_platform.comment_service.model.Comment;
import com.streaming_platform.comment_service.repository.CommentRepository;
import com.streaming_platform.comment_service.utils.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentRepository repository;

    public CommentResponse addComment(String userId, String videoId, CommentRequest request){
        Comment comment = Comment.builder()
                .userId(userId)
                .videoId(videoId)
                .content(request.getContent())
                .parentId(request.getParentId())
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(comment);

        return Mappers.mapToCommentResponse(comment);
    }

    public List<CommentResponse> getComments(String videoId){
        return repository
                .findByVideoIdAndParentIdIsNullOrderByCreatedAt(videoId)
                .stream()
                .map(Mappers::mapToCommentResponse)
                .collect(Collectors.toList());
    }
}
