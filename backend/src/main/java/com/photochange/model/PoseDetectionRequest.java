package com.photochange.model;

/**
 * 姿势检测请求
 */
public class PoseDetectionRequest {
    private String imageId;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}