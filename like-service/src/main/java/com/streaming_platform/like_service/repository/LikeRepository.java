package com.streaming_platform.like_service.repository;

import com.streaming_platform.like_service.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, String> {
    boolean existsByUserIdAndVideoId(String userId, String videoId);

    void deleteByUserIdAndVideoId(String userId, String videoId);

    long countByVideoId(String videoId);
}
