package com.streaming_platform.video_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateVideoRequest {
    private String title;
    private String description;
    private String rawVideoUrl;
}
