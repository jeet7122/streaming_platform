package com.streaming_platform.events;



public class VideoUploadEvent {
    private String userId;
    private String videoId;
    private String rawVideoUrl;

    public VideoUploadEvent(String userId, String videoId, String rawVideoUrl) {
        this.userId = userId;
        this.videoId = videoId;
        this.rawVideoUrl = rawVideoUrl;
    }

    public VideoUploadEvent() {
    }

    public String getRawVideoUrl() {
        return rawVideoUrl;
    }

    public void setRawVideoUrl(String rawVideoUrl) {
        this.rawVideoUrl = rawVideoUrl;
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
