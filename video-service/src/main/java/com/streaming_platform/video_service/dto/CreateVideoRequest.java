package com.streaming_platform.video_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateVideoRequest {
    private String title;
    private String description;
}
