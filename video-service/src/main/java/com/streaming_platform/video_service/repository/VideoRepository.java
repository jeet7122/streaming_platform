package com.streaming_platform.video_service.repository;

import com.streaming_platform.video_service.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, String> {
    List<Video> findByUserId(String userId);
}
