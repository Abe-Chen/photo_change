package com.photochange.model;

/**
 * 姿势检测响应
 */
public class PoseDetectionResponse {
    private String detectionId;
    private String imageId;
    private String status; // processing, completed, failed
    private String message;
    private Integer estimatedTime; // 预估处理时间（秒）

    public PoseDetectionResponse(String detectionId, String imageId, String status, String message, Integer estimatedTime) {
        this.detectionId = detectionId;
        this.imageId = imageId;
        this.status = status;
        this.message = message;
        this.estimatedTime = estimatedTime;
    }

    public String getDetectionId() {
        return detectionId;
    }

    public void setDetectionId(String detectionId) {
        this.detectionId = detectionId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
}