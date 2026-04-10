package com.streaming_platform.processing_service.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class VideoClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${video.service.uri}")
    private String baseUri;

    public void updateVideo(String videoId, String manifestUrl, String thumbnailUrl){
        String url = baseUri + videoId + "/complete";
        Map<String, String> body = new HashMap<>();
        body.put("manifestUrl", manifestUrl);
        body.put("thumbnailUrl", thumbnailUrl);
        log.info("Sending request to url: {}, with body: {}", url, body);
        restTemplate.put(url, body);
    }
}
