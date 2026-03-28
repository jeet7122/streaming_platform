package com.streaming_platform.comment_service.repository;

import com.streaming_platform.comment_service.dto.CommentResponse;
import com.streaming_platform.comment_service.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findByVideoIdAndParentIdIsNullOrderByCreatedAt(String videoId);
}
