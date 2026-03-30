package com.streaming_platform.processing_service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class VideoClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${video.service.uri}")
    private String baseUri;

    public void updateVideo(String videoId, String manifestUrl){
        String url = baseUri + videoId + "/complete";
        Map<String, String> body = new HashMap<>();
        body.put("manifestUrl", manifestUrl);
        restTemplate.put(url, body);
    }
}
