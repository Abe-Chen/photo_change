package com.photochange.model;

import java.util.List;
import java.util.Map;

/**
 * 姿势检测结果
 */
public class PoseDetectionResult {
    private String detectionId;
    private String imageId;
    private String status; // processing, completed, failed
    private List<Keypoint> keypoints;
    private Map<String, List<List<Float>>> segments;
    private Float confidence;
    private Long createdAt;
    private Long completedAt;
    private String error;

    public PoseDetectionResult() {
    }

    public PoseDetectionResult(String detectionId, String imageId, String status, List<Keypoint> keypoints,
                              Map<String, List<List<Float>>> segments, Float confidence, Long createdAt, String error) {
        this.detectionId = detectionId;
        this.imageId = imageId;
        this.status = status;
        this.keypoints = keypoints;
        this.segments = segments;
        this.confidence = confidence;
        this.createdAt = createdAt;
        this.error = error;
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

    public List<Keypoint> getKeypoints() {
        return keypoints;
    }

    public void setKeypoints(List<Keypoint> keypoints) {
        this.keypoints = keypoints;
    }

    public Map<String, List<List<Float>>> getSegments() {
        return segments;
    }

    public void setSegments(Map<String, List<List<Float>>> segments) {
        this.segments = segments;
    }

    public Float getConfidence() {
        return confidence;
    }

    public void setConfidence(Float confidence) {
        this.confidence = confidence;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Long completedAt) {
        this.completedAt = completedAt;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}