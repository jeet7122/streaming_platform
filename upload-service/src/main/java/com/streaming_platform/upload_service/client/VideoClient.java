package com.streaming_platform.upload_service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class VideoClient {
    private final RestTemplate restTemplate;

    @Value("${video-service-url}")
    private String videoServiceUrl;
    public void upload(String title, String description, String fileUrl, String userId){
        Map<String, String> body = new HashMap<>();
        body.put("title", title);
        body.put("description", description);
        body.put("rawFileUrl", fileUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-USER-ID", userId);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(videoServiceUrl, request, String.class);
    }
}
