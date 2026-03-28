package com.streaming_platform.comment_service.utils;

import com.streaming_platform.comment_service.dto.CommentResponse;
import com.streaming_platform.comment_service.model.Comment;

public class Mappers {
    public static CommentResponse mapToCommentResponse(Comment c){
        return CommentResponse.builder()
                .id(c.getId())
                .content(c.getContent())
                .userId(c.getUserId())
                .parentId(c.getParentId())
                .createdAt(c.getCreatedAt())
                .build();
    }
}
