package com.streaming_platform.events;


public class VideoUploadEvent {
    private String userId;
    private String videoId;

    public VideoUploadEvent(String userId, String videoId) {
        this.userId = userId;
        this.videoId = videoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
