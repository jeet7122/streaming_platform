package com.streaming_platform.video_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;
    private String description;
    private String userId;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    private String rawVideoUrl;
    private String manifestUrl;
    private LocalDateTime createdAt;
}
